package com.nttdata.bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.nttdata.bank.dto.MovementDto;
import com.nttdata.bank.entity.Account;
import com.nttdata.bank.entity.Movement;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.exception.InsufficientFundsException;
import com.nttdata.bank.exception.InvalidFieldException;
import com.nttdata.bank.exception.UnavailableEntityException;
import com.nttdata.bank.repository.AccountRepository;
import com.nttdata.bank.repository.MovementRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
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
  private final MovementDto dto = new MovementDto(null, "100200", 50.0, null, null);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    activeAccount = new Account();
    activeAccount.setId(1L);
    activeAccount.setAccountNumber("100200");
    activeAccount.setInitialBalance(100.0);
    activeAccount.setStatus(true);
  }

  @Test
  void testCreateCredit_success() {
    when(accountRepository.findByAccountNumber("100200"))
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

    assertEquals("100200", result.accountNumber());
    assertEquals(50.0, result.amount());
    assertEquals(150.0, result.balance());
  }

  @Test
  void testCreateDebit_success() {
    Movement lastMovement = new Movement();
    lastMovement.setBalance(100.0);

    when(accountRepository.findByAccountNumber("100200"))
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

    assertEquals("100200", result.accountNumber());
    assertEquals(-50.0, result.amount());
    assertEquals(50.0, result.balance());
  }

  @Test
  void testCreateDebit_exceedsBalance_throwsException() {
    MovementDto movementDto = new MovementDto(null, "100200", 100.01, null, null);

    when(accountRepository.findByAccountNumber("100200"))
      .thenReturn(Optional.of(activeAccount));
    when(movementRepository.findTopByAccountIdOrderByTimestampDesc(1L))
      .thenReturn(Optional.empty());

    assertThrows(InsufficientFundsException.class,
      () -> movementService.createDebit(movementDto));
  }

  @Test
  void testCreateMovement_accountNotFound_throwsException() {
    when(accountRepository.findByAccountNumber("ACC999"))
      .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
      () -> movementService.createCredit(dto));
  }

  @Test
  void testCreateMovement_inactiveAccount_throwsException() {
    activeAccount.setStatus(false);

    when(accountRepository.findByAccountNumber("100200"))
      .thenReturn(Optional.of(activeAccount));

    assertThrows(UnavailableEntityException.class,
      () -> movementService.createCredit(dto));
  }

  @Test
  void testCreateMovement_invalidAmount_throwsException() {
    MovementDto movementDto = new MovementDto(null, "100200", -10.0, null, null);

    when(accountRepository.findByAccountNumber("100200"))
      .thenReturn(Optional.of(activeAccount));

    assertThrows(InvalidFieldException.class,
      () -> movementService.createCredit(movementDto));
  }

  @Test
  void testGetAllMovements() {
    Movement movement = new Movement();
    movement.setId(1L);
    movement.setTimestamp(Timestamp.from(Instant.now()));
    movement.setType(Movement.MovementType.DEPOSIT);
    movement.setValue(50.0);
    movement.setBalance(150.0);
    movement.setAccount(activeAccount);

    when(movementRepository.findAll()).thenReturn(List.of(movement));

    List<MovementDto> result = movementService.getAllMovements();

    assertEquals(1, result.size());

    MovementDto resultDto = result.getFirst();
    assertNotNull(resultDto.timestamp());
    assertEquals("100200", resultDto.accountNumber());
    assertEquals(50.0, resultDto.amount());
    assertEquals(150.0, resultDto.balance());
  }
}