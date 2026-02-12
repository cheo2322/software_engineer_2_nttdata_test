package com.nttdata.bank.exception;

public class InvalidFieldException extends RuntimeException {

  public <T> InvalidFieldException(Class<T> clazz, String fieldName, String fieldValue) {
    super(
      String.format("Invalid value '%s' for field '%s' in class '%s'", fieldValue, fieldName,
        clazz.getSimpleName()
      )
    );
  }
}
