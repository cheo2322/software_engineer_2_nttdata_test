package com.nttdata.bank.exception;

public class InsufficientFundsException extends RuntimeException {

  public InsufficientFundsException() {
    super("Saldo no disponible");
  }
}
