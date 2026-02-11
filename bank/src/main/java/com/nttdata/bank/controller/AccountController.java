package com.nttdata.bank.controller;

import com.nttdata.bank.service.AccountService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }
}
