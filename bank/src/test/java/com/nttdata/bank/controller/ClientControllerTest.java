package com.nttdata.bank.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.nttdata.bank.dto.ClientDto;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.entity.Person.GenrePerson;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.exception.InvalidFieldException;
import com.nttdata.bank.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
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
  void shouldHandleInvalidFieldException_whenCreateClient() {
    ClientDto dto = new ClientDto("Sergio", "INVALID_GENRE", 29, "12345678", "123 St", "555-1234",
      "secret", "ACTIVE");

    doThrow(new InvalidFieldException(GenrePerson.class, "genre", "INVALID_GENRE"))
      .when(clientService).createClient(dto);

    restTestClient.post()
      .uri("/bank/v1/clients")
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody().jsonPath("$.message")
      .isEqualTo("Invalid value 'INVALID_GENRE' for field 'genre' in class 'GenrePerson'");
  }

  @Test
  void shouldHandleDataAccessException_whenCreateClient() {
    ClientDto dto = new ClientDto("Sergio", "MALE", 29, "12345678", "123 St", "555-1234",
      "secret", "ACTIVE");

    doThrow(new DataIntegrityViolationException("Database exception")).when(clientService)
      .createClient(dto);

    restTestClient.post()
      .uri("/bank/v1/clients")
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .exchange()
      .expectStatus().is5xxServerError()
      .expectBody().jsonPath("$.message")
      .isEqualTo("Database error occurred");
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
  void shouldHandleEntityNotFoundException_whenUpdateClient() {
    ClientDto dto = new ClientDto("Sergio", "MALE", 29, "12345678", "123 St", "555-1234", "secret",
      "ACTIVE");

    doThrow(new EntityNotFoundException(Client.class, "99"))
      .when(clientService).updateClient(99L, dto);

    restTestClient.put()
      .uri("/bank/v1/clients/99")
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .exchange()
      .expectStatus().isNotFound()
      .expectBody().jsonPath("$.message").isEqualTo("Client with id 99 not found");
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
  void shouldHandleEntityNotFoundException_whenUpdateClientPassword() {
    doThrow(new EntityNotFoundException(Client.class, "99"))
      .when(clientService).updateClientPassword(99L, "newSecret");

    restTestClient.patch()
      .uri("/bank/v1/clients/99/password?newPassword=newSecret")
      .exchange()
      .expectStatus().isNotFound()
      .expectBody().jsonPath("$.message").isEqualTo("Client with id 99 not found");
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

  @Test
  void shouldHandleEntityNotFoundException_whenDeleteClient() {
    doThrow(new EntityNotFoundException(Client.class, "99"))
      .when(clientService).deleteClient(99L);

    restTestClient.delete()
      .uri("/bank/v1/clients/99")
      .exchange()
      .expectStatus().isNotFound()
      .expectBody().jsonPath("$.message").isEqualTo("Client with id 99 not found");
  }
}
