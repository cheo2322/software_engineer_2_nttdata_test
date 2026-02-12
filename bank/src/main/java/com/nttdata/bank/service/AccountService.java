package com.nttdata.bank.service;

import com.nttdata.bank.dto.AccountDto;
import com.nttdata.bank.entity.Account;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.mapper.AccountMapper;
import com.nttdata.bank.repository.AccountRepository;
import com.nttdata.bank.repository.ClientRepository;
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

  public void activateAccount(Long accountId) {
    this.updateAccountStatus(accountId, true);
  }

  public void deleteAccount(Long accountId) {
    this.updateAccountStatus(accountId, false);
  }

  private void updateAccountStatus(Long accountId, boolean status) {
    Client client = clientRepository.findById(accountId)
      .orElseThrow(() -> new EntityNotFoundException(Account.class, String.valueOf(accountId)));

    client.setStatus(status);

    clientRepository.save(client);
  }
}
