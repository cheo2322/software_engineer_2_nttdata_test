package com.nttdata.bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.nttdata.bank.dto.Report;
import com.nttdata.bank.dto.ReportResponseDto;
import com.nttdata.bank.entity.Account;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.entity.Movement;
import com.nttdata.bank.entity.Movement.MovementType;
import com.nttdata.bank.entity.Person;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.repository.AccountRepository;
import com.nttdata.bank.repository.ClientRepository;
import com.nttdata.bank.repository.MovementRepository;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

  @Mock
  private ClientRepository clientRepository;
  @Mock
  private AccountRepository accountRepository;
  @Mock
  private MovementRepository movementRepository;

  @InjectMocks
  private ReportService reportService;

  private Client client;
  private Account account;
  private Movement movement;

  @BeforeEach
  void setUp() {
    Person person = new Person();
    person.setId(1L);
    person.setName("Test");
    person.setIdentification("1234567890");

    client = new Client();
    client.setId(10L);
    client.setPerson(person);
    client.setStatus(true);

    account = new Account();
    account.setId(100L);
    account.setAccountNumber("100200");
    account.setInitialBalance(500.00);
    account.setStatus(true);

    movement = new Movement();
    movement.setTimestamp(Timestamp.valueOf("2026-02-10 12:53:00"));
    movement.setAccount(account);
    movement.setType(MovementType.DEPOSIT);
    movement.setValue(50.0);
    movement.setBalance(150.0);
  }

  @Test
  void shouldGenerateReportSuccessfully() {
    when(clientRepository.findByPersonIdentification("1234567890"))
      .thenReturn(Optional.of(client));
    when(accountRepository.findAllByClientId(10L))
      .thenReturn(List.of(account));
    when(movementRepository.findAllByAccountsAndDateRange(
      anyList(),
      any(Timestamp.class),
      any(Timestamp.class)
    )).thenReturn(List.of(movement));

    ReportResponseDto response = reportService.getReport("01-02-2026", "10-02-2026", "1234567890");

    assertEquals(1, response.reports().size());
    Report report = response.reports().getFirst();
    assertEquals("100200", report.accountNumber());
    assertEquals("Test", report.client());
    assertEquals(150.0, report.availableBalance());

    verify(clientRepository).findByPersonIdentification("1234567890");
    verify(accountRepository).findAllByClientId(10L);
    verify(movementRepository).findAllByAccountsAndDateRange(anyList(), any(), any());
  }

  @Test
  void shouldThrowEntityNotFoundException_whenClientNotFound() {
    when(clientRepository.findByPersonIdentification("9999999999"))
      .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
      () -> reportService.getReport("2026-02-01", "2026-02-12", "9999999999"));

    verify(clientRepository).findByPersonIdentification("9999999999");
    verifyNoInteractions(accountRepository, movementRepository);
  }

  @Test
  void shouldThrowEntityNotFoundException_whenNoAccountsFound() {
    when(clientRepository.findByPersonIdentification("1234567890"))
      .thenReturn(Optional.of(client));
    when(accountRepository.findAllByClientId(10L))
      .thenReturn(List.of());

    assertThrows(EntityNotFoundException.class,
      () -> reportService.getReport("2026-02-01", "2026-02-12", "1234567890"));

    verify(clientRepository).findByPersonIdentification("1234567890");
    verify(accountRepository).findAllByClientId(10L);
    verifyNoInteractions(movementRepository);
  }

  @Test
  void shouldGenerateReportPdfSuccessfully() {
    when(clientRepository.findByPersonIdentification("1234567890"))
      .thenReturn(Optional.of(client));
    when(accountRepository.findAllByClientId(10L))
      .thenReturn(List.of(account));
    when(movementRepository.findAllByAccountsAndDateRange(anyList(), any(), any()))
      .thenReturn(List.of(movement));

    String pdfBase64 = reportService.getReportPdf("01-02-2026", "12-02-2026", "1234567890");

    assertNotNull(pdfBase64);
    assertFalse(pdfBase64.isEmpty());

    verify(clientRepository).findByPersonIdentification("1234567890");
    verify(accountRepository).findAllByClientId(10L);
    verify(movementRepository).findAllByAccountsAndDateRange(anyList(), any(), any());
  }
}