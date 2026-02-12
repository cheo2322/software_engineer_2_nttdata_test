package com.nttdata.bank.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nttdata.bank.dto.AccountDto;
import com.nttdata.bank.entity.Account;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.repository.AccountRepository;
import com.nttdata.bank.repository.ClientRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private ClientRepository clientRepository;

  @InjectMocks
  private AccountService accountService;

  private AccountDto accountDto;
  private Client client;

  @BeforeEach
  void setUp() {
    accountDto = new AccountDto("12345", "SAVINGS", 1000.0, true, 1L);

    client = new Client();
    client.setId(1L);
    client.setStatus(true);
  }

  @Test
  void testCreateAccount_success() {
    when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

    accountService.createAccount(accountDto);

    verify(accountRepository, times(1)).save(any(Account.class));
  }

  @Test
  void testCreateAccount_clientNotFound() {
    when(clientRepository.findById(99L)).thenReturn(Optional.empty());

    AccountDto dto = new AccountDto("12345", "SAVINGS", 1000.0, true, 99L);

    assertThrows(EntityNotFoundException.class,
      () -> accountService.createAccount(dto));

    verify(accountRepository, never()).save(any());
  }

  @Test
  void testActivateAccount_success() {
    when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

    accountService.activateAccount(1L);

    verify(clientRepository, times(1)).save(client);
    assertTrue(client.getStatus());
  }

  @Test
  void testActivateAccount_notFound() {
    when(clientRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
      () -> accountService.activateAccount(99L));

    verify(clientRepository, never()).save(any());
  }

  @Test
  void testDeleteAccount_success() {
    when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

    accountService.deleteAccount(1L);

    verify(clientRepository, times(1)).save(client);
    assertFalse(client.getStatus());
  }

  @Test
  void testDeleteAccount_notFound() {
    when(clientRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
      () -> accountService.deleteAccount(99L));

    verify(clientRepository, never()).save(any());
  }
}