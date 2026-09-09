package br.com.fiap.reservas.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Runs the HTTP contract against Oracle instead of H2 when explicitly enabled.
 *
 * Set RUN_ORACLE_INTEGRATION_TESTS=true together with the ORACLE_DB_* variables
 * before running this class. Each test creates uniquely named data and leaves
 * it in the database so the test can also be run against a shared local schema.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("oracle")
@EnabledIfEnvironmentVariable(named = "RUN_ORACLE_INTEGRATION_TESTS", matches = "true")
class OracleApiIntegrationTest {

    @Autowired MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void exercisesResourceEndpointsAndValidation() throws Exception {
        Fixtures fixtures = createResources();

        mockMvc.perform(get("/professores")).andExpect(status().isOk());
        mockMvc.perform(get("/professores/{id}", fixtures.professorId())).andExpect(status().isOk());
        mockMvc.perform(get("/salas")).andExpect(status().isOk());
        mockMvc.perform(get("/salas/{id}", fixtures.roomId())).andExpect(status().isOk());
        mockMvc.perform(get("/equipamentos")).andExpect(status().isOk());
        mockMvc.perform(get("/equipamentos/{id}", fixtures.activeEquipmentId())).andExpect(status().isOk());

        mockMvc.perform(patch("/equipamentos/{id}/status", fixtures.activeEquipmentId())
                        .param("ativo", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));
        mockMvc.perform(patch("/equipamentos/{id}/status", fixtures.activeEquipmentId())
                        .param("ativo", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(true));

        postJson("/professores", Map.of(
                "nome", "Duplicate " + fixtures.suffix(),
                "email", fixtures.professorEmail()))
                .andExpect(status().isConflict());
        postJson("/salas", Map.of("nome", fixtures.roomName()))
                .andExpect(status().isConflict());
        postJson("/equipamentos", Map.of(
                "nome", fixtures.activeEquipmentName(),
                "tipo", "Notebook"))
                .andExpect(status().isConflict());

        postJson("/professores", Map.of("nome", "", "email", "invalid-email"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{bad-json"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/professores/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
        mockMvc.perform(patch("/equipamentos/{id}/status", Long.MAX_VALUE)
                        .param("ativo", "false"))
                .andExpect(status().isNotFound());
    }

    @Test
    void exercisesReservationRules() throws Exception {
        Fixtures fixtures = createResources();
        LocalDateTime validStart = future(14);

        postJson("/reservas", reservation(fixtures, validStart, validStart.plusHours(2), List.of()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.equipmentSelection").exists());
        postJson("/reservas", reservation(fixtures, validStart.plusDays(1).plusHours(2),
                validStart.plusDays(1), List.of(fixtures.activeEquipmentId())))
                .andExpect(status().isBadRequest());
        postJson("/reservas", reservation(fixtures, future(1), future(1).plusHours(2),
                List.of(fixtures.activeEquipmentId())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type")
                        .value("https://reservas.fiapt.example/problems/minimum-advance-not-met"));
        postJson("/reservas", reservation(fixtures, future(16), future(16).plusHours(2),
                List.of(fixtures.inactiveEquipmentId())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type")
                        .value("https://reservas.fiapt.example/problems/inactive-equipment"));
        postJson("/reservas", reservationWithProfessor(fixtures, Long.MAX_VALUE, future(17),
                List.of(fixtures.activeEquipmentId())))
                .andExpect(status().isNotFound());

        postJson("/reservas", reservation(fixtures, validStart, validStart.plusHours(2),
                List.of(fixtures.activeEquipmentId())))
                .andExpect(status().isCreated());
        postJson("/reservas", reservation(fixtures, validStart.plusHours(1), validStart.plusHours(3),
                List.of(fixtures.activeEquipmentId())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type")
                        .value("https://reservas.fiapt.example/problems/equipment-unavailable"));
        postJson("/reservas", reservation(fixtures, validStart.plusHours(1), validStart.plusHours(3),
                List.of(fixtures.secondActiveEquipmentId())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type")
                        .value("https://reservas.fiapt.example/problems/room-unavailable"));
        postJson("/reservas", reservation(fixtures, validStart.plusHours(2), validStart.plusHours(4),
                List.of(fixtures.activeEquipmentId())))
                .andExpect(status().isCreated());

        postJson("/reservas", quantityReservation(fixtures, future(18)))
                .andExpect(status().isCreated());
    }

    @Test
    void exercisesReservationLifecycle() throws Exception {
        Fixtures fixtures = createResources();
        LocalDateTime start = future(20);
        long reservationId = postForId("/reservas", reservation(
                fixtures, start, start.plusHours(2), List.of(fixtures.activeEquipmentId())));

        mockMvc.perform(get("/reservas")).andExpect(status().isOk());
        mockMvc.perform(get("/reservas/{id}", reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMADA"));
        mockMvc.perform(delete("/reservas/{id}", reservationId))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/reservas/{id}", reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADA"));
        mockMvc.perform(delete("/reservas/{id}", reservationId))
                .andExpect(status().isConflict());
        mockMvc.perform(get("/reservas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    private Fixtures createResources() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String professorEmail = "oracle." + suffix + "@example.com";
        String professorName = "Oracle Professor " + suffix;
        String roomName = "OR-" + suffix;
        String activeEquipmentName = "Oracle Equipment A " + suffix;
        String secondActiveEquipmentName = "Oracle Equipment B " + suffix;
        String inactiveEquipmentName = "Oracle Equipment I " + suffix;

        long professorId = postForId("/professores", Map.of(
                "nome", professorName, "email", professorEmail));
        long roomId = postForId("/salas", Map.of("nome", roomName));
        long activeEquipmentId = postForId("/equipamentos", Map.of(
                "nome", activeEquipmentName, "tipo", "Notebook", "ativo", true));
        long secondActiveEquipmentId = postForId("/equipamentos", Map.of(
                "nome", secondActiveEquipmentName, "tipo", "Camera", "ativo", true));
        long inactiveEquipmentId = postForId("/equipamentos", Map.of(
                "nome", inactiveEquipmentName, "tipo", "Microfone", "ativo", false));

        return new Fixtures(suffix, professorId, professorEmail, roomId, roomName,
                activeEquipmentId, activeEquipmentName, secondActiveEquipmentId, inactiveEquipmentId);
    }

    private Map<String, Object> reservation(
            Fixtures fixtures,
            LocalDateTime start,
            LocalDateTime end,
            List<Long> equipmentIds) {
        return reservationWithProfessor(fixtures, fixtures.professorId(), start, equipmentIds,
                Map.of("entrega", end));
    }

    private Map<String, Object> reservationWithProfessor(
            Fixtures fixtures,
            long professorId,
            LocalDateTime start,
            List<Long> equipmentIds) {
        return reservationWithProfessor(fixtures, professorId, start, equipmentIds,
                Map.of("entrega", start.plusHours(2)));
    }

    private Map<String, Object> reservationWithProfessor(
            Fixtures fixtures,
            long professorId,
            LocalDateTime start,
            List<Long> equipmentIds,
            Map<String, Object> additionalFields) {
        Map<String, Object> request = new java.util.LinkedHashMap<>();
        request.put("professorId", professorId);
        request.put("curso", "Oracle Integration " + fixtures.suffix());
        request.put("salaId", fixtures.roomId());
        request.put("retirada", start);
        request.put("equipamentosIds", equipmentIds);
        request.putAll(additionalFields);
        return request;
    }

    private Map<String, Object> quantityReservation(Fixtures fixtures, LocalDateTime start) {
        return Map.of(
                "professorId", fixtures.professorId(),
                "curso", "Oracle Quantity " + fixtures.suffix(),
                "salaId", fixtures.roomId(),
                "retirada", start,
                "entrega", start.plusHours(2),
                "equipamentos", List.of(Map.of(
                        "equipamentoId", fixtures.secondActiveEquipmentId(),
                        "quantidade", 2)));
    }

    private long postForId(String path, Object body) throws Exception {
        MvcResult result = postJson(path, body)
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").longValue();
    }

    private ResultActions postJson(String path, Object body) throws Exception {
        return mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));
    }

    private LocalDateTime future(int days) {
        return LocalDateTime.now().plusDays(days).withHour(18).withMinute(0).withSecond(0).withNano(0);
    }

    private record Fixtures(
            String suffix,
            long professorId,
            String professorEmail,
            long roomId,
            String roomName,
            long activeEquipmentId,
            String activeEquipmentName,
            long secondActiveEquipmentId,
            long inactiveEquipmentId) {
    }
}
