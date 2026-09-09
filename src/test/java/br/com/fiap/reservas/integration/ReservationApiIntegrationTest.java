package br.com.fiap.reservas.integration;

import br.com.fiap.reservas.application.dto.request.ReservaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReservationApiIntegrationTest {

    @Autowired MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void createsReservationThroughHttpBoundary() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(10).withHour(18).withMinute(0).withSecond(0).withNano(0);
        ReservaRequest request = request(start, 1L);

        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.curso").value("Engenharia de Software"))
                .andExpect(jsonPath("$.status").value("CONFIRMADA"));
    }

    @Test
    void returnsProblemDetailForInvalidTimeRange() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(10).withHour(18).withMinute(0).withSecond(0).withNano(0);
        ReservaRequest request = new ReservaRequest(
                1L, "Engenharia de Software", 1L, start, start, Set.of(1L));

        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("https://reservas.fiapt.example/problems/invalid-time-range"))
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    void returnsProblemDetailForInactiveEquipment() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(11).withHour(18).withMinute(0).withSecond(0).withNano(0);
        ReservaRequest request = request(start, 4L);

        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value("https://reservas.fiapt.example/problems/inactive-equipment"))
                .andExpect(jsonPath("$.equipment[0]").value("Microfone 02"));
    }

    @Test
    void listsGetsAndCancelsReservation() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(12).withHour(18).withMinute(0).withSecond(0).withNano(0);
        var result = mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request(start, 2L))))
                .andExpect(status().isCreated())
                .andReturn();
        long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/reservas")).andExpect(status().isOk());
        mockMvc.perform(get("/reservas/{id}", id)).andExpect(status().isOk());
        mockMvc.perform(delete("/reservas/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/reservas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADA"));
    }

    @Test
    void returnsProblemDetailForOverlappingEquipment() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(30).withHour(18).withMinute(0).withSecond(0).withNano(0);
        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request(start, 1L))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request(start.plusHours(1), 1L))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value("https://reservas.fiapt.example/problems/equipment-unavailable"));
    }

    private ReservaRequest request(LocalDateTime start, Long equipmentId) {
        return new ReservaRequest(
                1L,
                "Engenharia de Software",
                1L,
                start,
                start.plusHours(2),
                Set.of(equipmentId));
    }
}
