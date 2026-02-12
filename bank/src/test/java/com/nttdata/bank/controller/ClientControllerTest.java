package com.nttdata.bank.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.nttdata.bank.dto.ClientDto;
import com.nttdata.bank.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

@WebMvcTest(ClientController.class)
@AutoConfigureRestTestClient
class ClientControllerTest {

  @Autowired
  private RestTestClient restTestClient;

  @MockitoBean
  private ClientService clientService;

  @Test
  void shouldCreateClient() {
    ClientDto dto = new ClientDto("Sergio", "MALE", 29, "12345678", "123 St", "555-1234", "secret",
      "ACTIVE");

    restTestClient.post()
      .uri("/bank/v1/clients")
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .exchange()
      .expectStatus().isCreated()
      .expectBody().jsonPath("$.message").isEqualTo("Client created successfully");

    verify(clientService, times(1)).createClient(dto);
  }

  @Test
  void shouldUpdateClient() {
    ClientDto dto = new ClientDto("Sergio", "MALE", 29, "12345678", "123 St", "555-1234", "secret",
      "ACTIVE");

    restTestClient.put()
      .uri("/bank/v1/clients/1")
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .exchange()
      .expectStatus().isOk()
      .expectBody().jsonPath("$.message").isEqualTo("Client updated successfully");

    verify(clientService, times(1)).updateClient(1L, dto);
  }

  @Test
  void shouldUpdateClientPassword() {
    restTestClient.patch()
      .uri("/bank/v1/clients/1/password?newPassword=newSecret")
      .exchange()
      .expectStatus().isOk()
      .expectBody().jsonPath("$.message").isEqualTo("Client password updated successfully");

    verify(clientService, times(1)).updateClientPassword(1L, "newSecret");
  }

  @Test
  void shouldDeleteClient() {
    restTestClient.delete()
      .uri("/bank/v1/clients/1")
      .exchange()
      .expectStatus().isOk()
      .expectBody().jsonPath("$.message").isEqualTo("Client deleted successfully");

    verify(clientService, times(1)).deleteClient(1L);
  }
}
