package com.nttdata.bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.nttdata.bank.entity.Account.AccountType;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.repository.AccountRepository;
import com.nttdata.bank.repository.ClientRepository;
import java.util.List;
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
  private Account account;

  @BeforeEach
  void setUp() {
    accountDto = new AccountDto(1L, "12345", "SAVINGS", 1000.0, true, 1L);

    client = new Client();
    client.setId(1L);
    client.setStatus(true);

    account = new Account();
    account.setId(10L);
    account.setAccountNumber("12345");
    account.setType(AccountType.SAVINGS);
    account.setStatus(true);
    account.setClient(client);
    account.setInitialBalance(100.00);
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

    AccountDto dto = new AccountDto(1L, "12345", "SAVINGS", 1000.0, true, 99L);

    assertThrows(EntityNotFoundException.class,
      () -> accountService.createAccount(dto));

    verify(accountRepository, never()).save(any());
  }

  @Test
  void testActivateAccount_success() {
    when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

    accountService.activateAccount("12345");

    verify(accountRepository, times(1)).save(account);
    assertTrue(account.getStatus());
  }

  @Test
  void testActivateAccount_notFound() {
    when(accountRepository.findByAccountNumber("99999")).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
      () -> accountService.activateAccount("99999"));

    verify(accountRepository, never()).save(any());
  }

  @Test
  void testDeleteAccount_success() {
    when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

    accountService.deleteAccount("12345");

    verify(accountRepository, times(1)).save(account);
    assertFalse(account.getStatus());
  }

  @Test
  void testDeleteAccount_notFound() {
    when(accountRepository.findByAccountNumber("99999")).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
      () -> accountService.deleteAccount("99999"));

    verify(accountRepository, never()).save(any());
  }

  @Test
  void testGetAllAccounts() {
    when(accountRepository.findAll()).thenReturn(List.of(account));

    List<AccountDto> allAccounts = accountService.getAllAccounts();

    assertEquals(1, allAccounts.size());

    AccountDto responseDto = allAccounts.getFirst();
    assertEquals(10L, responseDto.id());
    assertEquals(account.getAccountNumber(), responseDto.number());
    assertEquals("SAVINGS", responseDto.type());
    assertEquals(100.0, responseDto.initialBalance());
    assertTrue(responseDto.status());
    assertEquals(1L, responseDto.clientId());

    verify(accountRepository, times(1)).findAll();
  }
}