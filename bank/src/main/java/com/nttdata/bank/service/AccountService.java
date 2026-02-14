package com.nttdata.bank.service;

import com.nttdata.bank.dto.AccountDto;
import com.nttdata.bank.entity.Account;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.mapper.AccountMapper;
import com.nttdata.bank.repository.AccountRepository;
import com.nttdata.bank.repository.ClientRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository accountRepository;
  private final ClientRepository clientRepository;

  private static final AccountMapper ACCOUNT_MAPPER = AccountMapper.INSTANCE;

  public AccountService(AccountRepository accountRepository, ClientRepository clientRepository) {
    this.accountRepository = accountRepository;
    this.clientRepository = clientRepository;
  }

  public void createAccount(AccountDto accountDto) {
    Long clientId = accountDto.clientId();
    Client client = clientRepository.findById(clientId)
      .orElseThrow(() -> new EntityNotFoundException(Client.class, String.valueOf(clientId)));

    Account account = ACCOUNT_MAPPER.dtoToEntity(accountDto);
    account.setClient(client);

    accountRepository.save(account);
  }

  public void activateAccount(String accountNumber) {
    this.updateAccountStatus(accountNumber, true);
  }

  public void deleteAccount(String accountNumber) {
    this.updateAccountStatus(accountNumber, false);
  }

  private void updateAccountStatus(String accountNumber, boolean status) {
    Account account = accountRepository.findByAccountNumber(accountNumber)
      .orElseThrow(() -> new EntityNotFoundException(Account.class, accountNumber));

    account.setStatus(status);
    accountRepository.save(account);
  }

  public List<AccountDto> getAllAccounts() {
      return accountRepository.findAll().stream()
        .map(ACCOUNT_MAPPER::entityToDto)
        .toList();
  }
}
