package com.nttdata.bank.exception;

public class DebitExceedsBalanceException extends RuntimeException {

  public DebitExceedsBalanceException(Double debitAmount, Double limitAmount) {
    super(
      String.format("Debit amount '%.2f' exceeds the balance amount: '%.2f'", debitAmount,
        limitAmount
      )
    );
  }
}
