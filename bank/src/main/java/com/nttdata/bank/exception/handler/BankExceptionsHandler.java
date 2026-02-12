package com.nttdata.bank.exception.handler;

import com.nttdata.bank.dto.BankResponse;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.exception.InvalidFieldException;
import lombok.extern.slf4j.Slf4j;
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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<BankResponse<Void>> handleGenericException(Exception ex) {
    BankResponse<Void> response = new BankResponse<>("999", "An unexpected error occurred", null);

    log.error(ex.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
