package com.nttdata.bank.exception.handler;

import com.nttdata.bank.dto.BankResponse;
import com.nttdata.bank.exception.InsufficientFundsException;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.exception.InvalidFieldException;
import com.nttdata.bank.exception.UnavailableEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BankExceptionsHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<BankResponse<Void>> handleEntityNotFoundException(
    EntityNotFoundException ex) {

    BankResponse<Void> response = new BankResponse<>("004", ex.getMessage(), null);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(InvalidFieldException.class)
  public ResponseEntity<BankResponse<Void>> handleInvalidFieldException(
    InvalidFieldException ex) {

    BankResponse<Void> response = new BankResponse<>("001", ex.getMessage(), null);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<BankResponse<Void>> handleDataAccessException(DataAccessException ex) {
    log.error(ex.getMessage());

    BankResponse<Void> response = new BankResponse<>("998", "Database error occurred", null);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(UnavailableEntityException.class)
  public ResponseEntity<BankResponse<Void>> handleUnavailableEntityException(
    UnavailableEntityException ex) {

    BankResponse<Void> response = new BankResponse<>("004", ex.getMessage(), null);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(InsufficientFundsException.class)
  public ResponseEntity<BankResponse<Void>> handleDebitExceedsBalanceException(
    InsufficientFundsException ex
  ) {
    BankResponse<Void> response = new BankResponse<>("001", ex.getMessage(), null);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<BankResponse<Void>> handleGenericException(Exception ex) {
    log.error(ex.getMessage());

    BankResponse<Void> response = new BankResponse<>("999", "An unexpected error occurred", null);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
