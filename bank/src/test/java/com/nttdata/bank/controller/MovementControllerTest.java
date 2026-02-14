package com.nttdata.bank.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nttdata.bank.dto.MovementDto;
import com.nttdata.bank.entity.Account;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.exception.InsufficientFundsException;
import com.nttdata.bank.service.MovementService;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

@WebMvcTest(MovementController.class)
@AutoConfigureRestTestClient
class MovementControllerTest {

  @Autowired
  private RestTestClient restTestClient;

  @MockitoBean
  private MovementService movementService;

  @Test
  void shouldCreateCredit() {
    MovementDto dto = new MovementDto(null, "ACC123", 50.0, null);
    MovementDto responseDto = new MovementDto("2026-02-12T10:00:00", "ACC123", 50.0, 150.0);

    when(movementService.createCredit(dto)).thenReturn(responseDto);

    restTestClient.post()
      .uri("/bank/v1/movements/credit")
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .exchange()
      .expectStatus().isOk()
      .expectBody().jsonPath("$.message").isEqualTo("Credit created successfully")
      .jsonPath("$.data.accountNumber").isEqualTo("ACC123")
      .jsonPath("$.data.amount").isEqualTo(50.0)
      .jsonPath("$.data.balance").isEqualTo(150.0);

    verify(movementService, times(1)).createCredit(dto);
  }

  @Test
  void shouldHandleEntityNotFoundException_whenCreateCredit() {
    MovementDto dto = new MovementDto(null, "ACC999", 50.0, null);

    doThrow(new EntityNotFoundException(Account.class, "ACC999"))
      .when(movementService).createCredit(dto);

    restTestClient.post()
      .uri("/bank/v1/movements/credit")
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .exchange()
      .expectStatus().isNotFound()
      .expectBody().jsonPath("$.message").isEqualTo("Account with id ACC999 not found");
  }

  @Test
  void shouldCreateDebit() {
    MovementDto dto = new MovementDto(null, "ACC123", 40.0, null);
    MovementDto responseDto = new MovementDto("2026-02-12T10:05:00", "ACC123", -40.0, 60.0);

    when(movementService.createDebit(dto)).thenReturn(responseDto);

    restTestClient.post()
      .uri("/bank/v1/movements/debit")
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .exchange()
      .expectStatus().isOk()
      .expectBody().jsonPath("$.message").isEqualTo("Debit created successfully")
      .jsonPath("$.data.accountNumber").isEqualTo("ACC123")
      .jsonPath("$.data.amount").isEqualTo(-40.0)
      .jsonPath("$.data.balance").isEqualTo(60.0);

    verify(movementService, times(1)).createDebit(dto);
  }

  @Test
  void shouldHandleDebitExceedsBalanceException_whenCreateDebit() {
    MovementDto dto = new MovementDto(null, "ACC123", 200.0, null);

    doThrow(new InsufficientFundsException())
      .when(movementService).createDebit(dto);

    restTestClient.post()
      .uri("/bank/v1/movements/debit")
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody().jsonPath("$.message")
      .isEqualTo("Saldo no disponible");
  }

  @Test
  void shouldGetMovements() {
    MovementDto dto = new MovementDto(Timestamp.from(Instant.now()).toString(), "100200", 200.0,
      null);

    when(movementService.getAllMovements()).thenReturn(List.of(dto));

    restTestClient.get()
      .uri("/bank/v1/movements")
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.code").isEqualTo("000")
      .jsonPath("$.message").isEqualTo("Movements retrieved successfully")
      .jsonPath("$.data[0].accountNumber").isEqualTo("100200")
      .jsonPath("$.data[0].amount").isEqualTo(200.0);

    verify(movementService, times(1)).getAllMovements();
  }

  @Test
  void shouldReturnEmptyList_whenGetMovements() {
    when(movementService.getAllMovements()).thenReturn(List.of());

    restTestClient.get()
      .uri("/bank/v1/movements")
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.code").isEqualTo("000")
      .jsonPath("$.message").isEqualTo("Movements retrieved successfully")
      .jsonPath("$.data").isEmpty();

    verify(movementService, times(1)).getAllMovements();
  }
}