package com.nttdata.bank.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nttdata.bank.dto.AccountDto;
import com.nttdata.bank.entity.Account;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.service.AccountService;
import java.util.List;
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

  private final AccountDto dto = new AccountDto(1L, "12345", "SAVINGS", 1000.0, true, 1L, "Test");

  @Test
  void shouldCreateAccount() {
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

    verify(accountService, times(1)).activateAccount("1");
  }

  @Test
  void shouldHandleEntityNotFoundException_whenActivateAccount() {
    doThrow(new EntityNotFoundException(Account.class, "99"))
      .when(accountService).activateAccount("99");

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

    verify(accountService, times(1)).deleteAccount("1");
  }

  @Test
  void shouldHandleEntityNotFoundException_whenDeleteAccount() {
    doThrow(new EntityNotFoundException(Account.class, "99"))
      .when(accountService).deleteAccount("99");

    restTestClient.delete()
      .uri("/bank/v1/accounts/99")
      .exchange()
      .expectStatus().isNotFound()
      .expectBody().jsonPath("$.message").isEqualTo("Account with id 99 not found");
  }

  @Test
  void shouldGetAllAccounts() {
    when(accountService.getAllAccounts()).thenReturn(List.of(dto));

    restTestClient.get()
      .uri("/bank/v1/accounts")
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.code").isEqualTo("000")
      .jsonPath("$.message").isEqualTo("Accounts retrieved successfully")
      .jsonPath("$.data.length()").isEqualTo(1)
      .jsonPath("$.data[0].id").isEqualTo(1L)
      .jsonPath("$.data[0].number").isEqualTo("12345")
      .jsonPath("$.data[0].type").isEqualTo("SAVINGS")
      .jsonPath("$.data[0].initialBalance").isEqualTo(1000.0)
      .jsonPath("$.data[0].status").isEqualTo(true)
      .jsonPath("$.data[0].clientId").isEqualTo(1L)
      .jsonPath("$.data[0].clientName").isEqualTo("Test");

    verify(accountService, times(1)).getAllAccounts();
  }

  @Test
  void shouldReturnEmptyList_whenGetAllAccounts() {
    restTestClient.get()
      .uri("/bank/v1/accounts")
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.code").isEqualTo("000")
      .jsonPath("$.message").isEqualTo("Accounts retrieved successfully")
      .jsonPath("$.data").isEmpty();

    verify(accountService, times(1)).getAllAccounts();
  }
}