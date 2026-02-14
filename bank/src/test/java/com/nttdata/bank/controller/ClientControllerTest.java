package com.nttdata.bank.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nttdata.bank.dto.ClientDto;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.entity.Person.GenrePerson;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.exception.InvalidFieldException;
import com.nttdata.bank.service.ClientService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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

  private ClientDto clientDto;

  @BeforeEach
  void setup() {
    clientDto = new ClientDto(1L, "Sergio", "MALE", 29, "12345678", "123 St", "555-1234",
      "secret", true);
  }

  @Test
  void shouldCreateClient() {
    restTestClient.post()
      .uri("/bank/v1/clients")
      .contentType(MediaType.APPLICATION_JSON)
      .body(clientDto)
      .exchange()
      .expectStatus().isCreated()
      .expectBody().jsonPath("$.message").isEqualTo("Client created successfully");

    verify(clientService, times(1)).createClient(clientDto);
  }

  @Test
  void shouldHandleInvalidFieldException_whenCreateClient() {
    ClientDto invalidClientDto = new ClientDto(1L, "Sergio", "INVALID_GENRE", 29, "12345678",
      "123 St", "555-1234", "secret", true);

    doThrow(new InvalidFieldException(GenrePerson.class, "genre", "INVALID_GENRE"))
      .when(clientService).createClient(invalidClientDto);

    restTestClient.post()
      .uri("/bank/v1/clients")
      .contentType(MediaType.APPLICATION_JSON)
      .body(invalidClientDto)
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody().jsonPath("$.message")
      .isEqualTo("Invalid value 'INVALID_GENRE' for field 'genre' in class 'GenrePerson'");
  }

  @Test
  void shouldHandleDataAccessException_whenCreateClient() {
    doThrow(new DataIntegrityViolationException("Database exception")).when(clientService)
      .createClient(clientDto);

    restTestClient.post()
      .uri("/bank/v1/clients")
      .contentType(MediaType.APPLICATION_JSON)
      .body(clientDto)
      .exchange()
      .expectStatus().is5xxServerError()
      .expectBody().jsonPath("$.message")
      .isEqualTo("Database error occurred");
  }

  @Test
  void shouldUpdateClient() {
    restTestClient.put()
      .uri("/bank/v1/clients/1")
      .contentType(MediaType.APPLICATION_JSON)
      .body(clientDto)
      .exchange()
      .expectStatus().isOk()
      .expectBody().jsonPath("$.message").isEqualTo("Client updated successfully");

    verify(clientService, times(1)).updateClient(1L, clientDto);
  }

  @Test
  void shouldHandleEntityNotFoundException_whenUpdateClient() {
    doThrow(new EntityNotFoundException(Client.class, "99"))
      .when(clientService).updateClient(99L, clientDto);

    restTestClient.put()
      .uri("/bank/v1/clients/99")
      .contentType(MediaType.APPLICATION_JSON)
      .body(clientDto)
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

  @Test
  void shouldGetAllClients() {
    when(clientService.getAllClients())
      .thenReturn(List.of(clientDto));

    restTestClient.get()
      .uri("/bank/v1/clients")
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.code").isEqualTo("000")
      .jsonPath("$.message").isEqualTo("Clients retrieved successfully")
      .jsonPath("$.data[0].id").isEqualTo(1L)
      .jsonPath("$.data[0].name").isEqualTo("Sergio");

    verify(clientService, times(1)).getAllClients();
  }

  @Test
  void shouldReturnEmptyList_whenGetAllClients() {
    when(clientService.getAllClients())
      .thenReturn(List.of());

    restTestClient.get()
      .uri("/bank/v1/clients")
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.code").isEqualTo("000")
      .jsonPath("$.message").isEqualTo("Clients retrieved successfully")
      .jsonPath("$.data").isEmpty();

    verify(clientService, times(1)).getAllClients();
  }
}
