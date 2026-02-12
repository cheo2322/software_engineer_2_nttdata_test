package com.nttdata.bank.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.nttdata.bank.dto.AccountDto;
import com.nttdata.bank.entity.Account;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

@WebMvcTest(AccountController.class)
@AutoConfigureRestTestClient
class AccountControllerTest {

  @Autowired
  private RestTestClient restTestClient;

  @MockitoBean
  private AccountService accountService;

  @Test
  void shouldCreateAccount() {
    AccountDto dto = new AccountDto("12345", "SAVINGS", 1000.0, true, 1L);

    restTestClient.post()
      .uri("/bank/v1/accounts")
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .exchange()
      .expectStatus().isCreated()
      .expectBody().jsonPath("$.message").isEqualTo("Account created successfully");

    verify(accountService, times(1)).createAccount(dto);
  }

  @Test
  void shouldHandleEntityNotFoundException_whenCreateAccount() {
    AccountDto dto = new AccountDto("12345", "SAVINGS", 1000.0, true, 99L);

    doThrow(new EntityNotFoundException(Client.class, "99"))
      .when(accountService).createAccount(dto);

    restTestClient.post()
      .uri("/bank/v1/accounts")
      .contentType(MediaType.APPLICATION_JSON)
      .body(dto)
      .exchange()
      .expectStatus().isNotFound()
      .expectBody().jsonPath("$.message").isEqualTo("Client with id 99 not found");
  }

  @Test
  void shouldActivateAccount() {
    restTestClient.patch()
      .uri("/bank/v1/accounts/1/status")
      .exchange()
      .expectStatus().isOk()
      .expectBody().jsonPath("$.message").isEqualTo("Account status updated successfully");

    verify(accountService, times(1)).activateAccount(1L);
  }

  @Test
  void shouldHandleEntityNotFoundException_whenActivateAccount() {
    doThrow(new EntityNotFoundException(Account.class, "99"))
      .when(accountService).activateAccount(99L);

    restTestClient.patch()
      .uri("/bank/v1/accounts/99/status")
      .exchange()
      .expectStatus().isNotFound()
      .expectBody().jsonPath("$.message").isEqualTo("Account with id 99 not found");
  }

  @Test
  void shouldDeleteAccount() {
    restTestClient.delete()
      .uri("/bank/v1/accounts/1")
      .exchange()
      .expectStatus().isOk()
      .expectBody().jsonPath("$.message").isEqualTo("Account deleted successfully");

    verify(accountService, times(1)).deleteAccount(1L);
  }

  @Test
  void shouldHandleEntityNotFoundException_whenDeleteAccount() {
    doThrow(new EntityNotFoundException(Account.class, "99"))
      .when(accountService).deleteAccount(99L);

    restTestClient.delete()
      .uri("/bank/v1/accounts/99")
      .exchange()
      .expectStatus().isNotFound()
      .expectBody().jsonPath("$.message").isEqualTo("Account with id 99 not found");
  }
}