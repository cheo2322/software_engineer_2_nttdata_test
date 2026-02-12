package com.nttdata.bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.nttdata.bank.dto.MovementDto;
import com.nttdata.bank.entity.Account;
import com.nttdata.bank.entity.Movement;
import com.nttdata.bank.exception.InsufficientFundsException;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.exception.InvalidFieldException;
import com.nttdata.bank.exception.UnavailableEntityException;
import com.nttdata.bank.repository.AccountRepository;
import com.nttdata.bank.repository.MovementRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MovementServiceTest {

  @Mock
  private MovementRepository movementRepository;

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private MovementService movementService;

  private Account activeAccount;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    activeAccount = new Account();
    activeAccount.setId(1L);
    activeAccount.setAccountNumber("ACC123");
    activeAccount.setInitialBalance(100.0);
    activeAccount.setStatus(true);
  }

  @Test
  void testCreateCredit_success() {
    MovementDto dto = new MovementDto(null, "ACC123", 50.0, null);

    when(accountRepository.findByAccountNumber("ACC123"))
      .thenReturn(Optional.of(activeAccount));
    when(movementRepository.findTopByAccountIdOrderByTimestampDesc(1L))
      .thenReturn(Optional.empty());

    Movement savedMovement = new Movement();
    savedMovement.setId(1L);
    savedMovement.setTimestamp(Timestamp.from(Instant.now()));
    savedMovement.setType(Movement.MovementType.DEPOSIT);
    savedMovement.setValue(50.0);
    savedMovement.setBalance(150.0);
    savedMovement.setAccount(activeAccount);

    when(movementRepository.save(any(Movement.class))).thenReturn(savedMovement);

    MovementDto result = movementService.createCredit(dto);

    assertEquals("ACC123", result.accountNumber());
    assertEquals(50.0, result.amount());
    assertEquals(150.0, result.balance());
  }

  @Test
  void testCreateDebit_success() {
    MovementDto dto = new MovementDto(null, "ACC123", 40.0, null);
    Movement lastMovement = new Movement();
    lastMovement.setBalance(100.0);

    when(accountRepository.findByAccountNumber("ACC123"))
      .thenReturn(Optional.of(activeAccount));
    when(movementRepository.findTopByAccountIdOrderByTimestampDesc(1L))
      .thenReturn(Optional.of(lastMovement));

    Movement savedMovement = new Movement();
    savedMovement.setId(1L);
    savedMovement.setTimestamp(Timestamp.from(Instant.now()));
    savedMovement.setType(Movement.MovementType.WITHDRAWAL);
    savedMovement.setValue(40.0);
    savedMovement.setBalance(60.0);
    savedMovement.setAccount(activeAccount);

    when(movementRepository.save(any(Movement.class))).thenReturn(savedMovement);

    MovementDto result = movementService.createDebit(dto);

    assertEquals("ACC123", result.accountNumber());
    assertEquals(-40.0, result.amount()); // signedAmount
    assertEquals(60.0, result.balance());
  }

  @Test
  void testCreateDebit_exceedsBalance_throwsException() {
    MovementDto dto = new MovementDto(null, "ACC123", 200.0, null);

    when(accountRepository.findByAccountNumber("ACC123"))
      .thenReturn(Optional.of(activeAccount));
    when(movementRepository.findTopByAccountIdOrderByTimestampDesc(1L))
      .thenReturn(Optional.empty());

    assertThrows(InsufficientFundsException.class,
      () -> movementService.createDebit(dto));
  }

  @Test
  void testCreateMovement_accountNotFound_throwsException() {
    MovementDto dto = new MovementDto(null, "ACC999", 50.0, null);

    when(accountRepository.findByAccountNumber("ACC999"))
      .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
      () -> movementService.createCredit(dto));
  }

  @Test
  void testCreateMovement_inactiveAccount_throwsException() {
    activeAccount.setStatus(false);
    MovementDto dto = new MovementDto(null, "ACC123", 50.0, null);

    when(accountRepository.findByAccountNumber("ACC123"))
      .thenReturn(Optional.of(activeAccount));

    assertThrows(UnavailableEntityException.class,
      () -> movementService.createCredit(dto));
  }

  @Test
  void testCreateMovement_invalidAmount_throwsException() {
    MovementDto dto = new MovementDto(null, "ACC123", 0.0, null);

    when(accountRepository.findByAccountNumber("ACC123"))
      .thenReturn(Optional.of(activeAccount));

    assertThrows(InvalidFieldException.class,
      () -> movementService.createCredit(dto));
  }
}