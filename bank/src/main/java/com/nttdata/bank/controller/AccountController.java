package com.nttdata.bank.controller;

import com.nttdata.bank.dto.AccountDto;
import com.nttdata.bank.dto.BankResponse;
import com.nttdata.bank.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank/v1/accounts")
public class AccountController {

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping
  public ResponseEntity<BankResponse<Void>> createAccount(@RequestBody AccountDto accountDto) {
    accountService.createAccount(accountDto);
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(new BankResponse<>("000", "Account created successfully", null));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<BankResponse<Void>> activateAccount(
    @PathVariable("id") Long accountId) {
    accountService.activateAccount(accountId);
    return ResponseEntity.ok(
      new BankResponse<>("000", "Account status updated successfully", null));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BankResponse<Void>> deleteAccount(@PathVariable("id") Long accountId) {
    accountService.deleteAccount(accountId);
    return ResponseEntity.ok(new BankResponse<>("000", "Account deleted successfully", null));
  }
}
