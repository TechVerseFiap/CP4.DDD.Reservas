package br.com.fiap.reservas.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ResourceApiIntegrationTest {

    @Autowired MockMvc mockMvc;

    @Test
    void exposesProfessorEndpoints() throws Exception {
        mockMvc.perform(post("/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Integration Professor\",\"email\":\"integration-"
                                + System.nanoTime() + "@example.com\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/professores")).andExpect(status().isOk());
        mockMvc.perform(get("/professores/999999")).andExpect(status().isNotFound());
    }

    @Test
    void exposesRoomEndpoints() throws Exception {
        mockMvc.perform(post("/salas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Room-" + System.nanoTime() + "\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/salas")).andExpect(status().isOk());
        mockMvc.perform(get("/salas/999999")).andExpect(status().isNotFound());
    }

    @Test
    void exposesEquipmentEndpoints() throws Exception {
        mockMvc.perform(post("/equipamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Integration Equipment " + System.nanoTime()
                                + "\",\"tipo\":\"Notebook\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/equipamentos")).andExpect(status().isOk());
        mockMvc.perform(get("/equipamentos/999999")).andExpect(status().isNotFound());
    }

    @Test
    void returnsNotFoundForUnknownEndpoint() throws Exception {
        mockMvc.perform(get("/not-a-real-endpoint"))
                .andExpect(status().isNotFound());
    }
}
