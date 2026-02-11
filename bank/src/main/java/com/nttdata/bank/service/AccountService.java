package com.nttdata.bank.service;

import com.nttdata.bank.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }
}
