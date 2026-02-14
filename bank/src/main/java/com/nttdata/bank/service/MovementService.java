package com.nttdata.bank.service;

import com.nttdata.bank.dto.MovementDto;
import com.nttdata.bank.entity.Account;
import com.nttdata.bank.entity.Movement;
import com.nttdata.bank.entity.Movement.MovementType;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.exception.InsufficientFundsException;
import com.nttdata.bank.exception.InvalidFieldException;
import com.nttdata.bank.exception.UnavailableEntityException;
import com.nttdata.bank.mapper.MovementMapper;
import com.nttdata.bank.repository.AccountRepository;
import com.nttdata.bank.repository.MovementRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MovementService {

  private final MovementRepository movementRepository;
  private final AccountRepository accountRepository;

  private static final MovementMapper MOVEMENT_MAPPER = MovementMapper.INSTANCE;

  public MovementService(MovementRepository movementRepository,
    AccountRepository accountRepository) {
    this.movementRepository = movementRepository;
    this.accountRepository = accountRepository;
  }

  public MovementDto createCredit(MovementDto creditDto) {
    return createMovement(creditDto, MovementType.DEPOSIT);
  }

  public MovementDto createDebit(MovementDto debitDto) {
    return createMovement(debitDto, MovementType.WITHDRAWAL);
  }

  private MovementDto createMovement(MovementDto dto, MovementType type) {
    String accountNumber = dto.accountNumber();
    Account account = accountRepository.findByAccountNumber(accountNumber)
      .orElseThrow(() -> new EntityNotFoundException(Account.class, accountNumber));

    if (Boolean.FALSE.equals(account.getStatus())) {
      throw new UnavailableEntityException(Account.class, accountNumber);
    }

    if (dto.amount() <= 0) {
      throw new InvalidFieldException(Movement.class, "amount", String.valueOf(dto.amount()));
    }

    double balance = account.getInitialBalance();
    Optional<Movement> lastMovementOpt = movementRepository
      .findTopByAccountIdOrderByTimestampDesc(account.getId());

    if (lastMovementOpt.isPresent()) {
      balance = lastMovementOpt.get().getBalance();
    }

    double signedAmount = type == MovementType.DEPOSIT ? dto.amount() : -dto.amount();
    double newBalance = balance + signedAmount;

    if (type == MovementType.WITHDRAWAL && newBalance < 0) {
      throw new InsufficientFundsException();
    }

    Movement newMovement = new Movement();
    newMovement.setType(type);
    newMovement.setValue(dto.amount());
    newMovement.setBalance(newBalance);
    newMovement.setAccount(account);

    Movement savedMovement = movementRepository.save(newMovement);

    return new MovementDto(
      savedMovement.getTimestamp().toString(),
      account.getAccountNumber(),
      signedAmount,
      newBalance
    );
  }

  public List<MovementDto> getAllMovements() {
    return movementRepository.findAll().stream()
      .map(MOVEMENT_MAPPER::entityToDto)
      .toList();
  }
}