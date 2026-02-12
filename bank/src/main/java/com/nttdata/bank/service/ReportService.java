package com.nttdata.bank.service;

import com.nttdata.bank.dto.Report;
import com.nttdata.bank.dto.ReportResponseDto;
import com.nttdata.bank.entity.Account;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.entity.Movement;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.repository.AccountRepository;
import com.nttdata.bank.repository.ClientRepository;
import com.nttdata.bank.repository.MovementRepository;
import com.nttdata.bank.util.TimeUtil;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

  private final ClientRepository clientRepository;
  private final AccountRepository accountRepository;
  private final MovementRepository movementRepository;

  public ReportService(ClientRepository clientRepository, AccountRepository accountRepository,
    MovementRepository movementRepository) {
    this.clientRepository = clientRepository;
    this.accountRepository = accountRepository;
    this.movementRepository = movementRepository;
  }

  public ReportResponseDto getReport(
    String initialDate,
    String finalDate,
    String clientIdentification
  ) {
    Client client = clientRepository.findByPersonIdentification(clientIdentification)
      .orElseThrow(
        () -> new EntityNotFoundException(Client.class, "identification", clientIdentification)
      );

    List<Account> accounts = accountRepository.findAllByClientId(client.getId());

    if (accounts.isEmpty()) {
      throw new EntityNotFoundException(Account.class, "client", String.valueOf(client.getId()));
    }

    List<Long> accountIds = accounts.stream().map(Account::getId).toList();
    List<Movement> movements = movementRepository.findAllByAccountsAndDateRange(
      accountIds,
      TimeUtil.getTimestampStartOfDay(initialDate),
      TimeUtil.getTimestampEndOfDay(finalDate)
    );

    List<Report> reports = movements.stream()
      .map(movement -> new Report(
        TimeUtil.getDateFromTimestamp(movement.getTimestamp()),
        client.getPerson().getName(),
        movement.getAccount().getAccountNumber(),
        movement.getType().name(),
        movement.getAccount().getInitialBalance(),
        movement.getAccount().getStatus(),
        movement.getValue(),
        movement.getBalance()
      ))
      .toList();

    return new ReportResponseDto(reports);
  }
}
