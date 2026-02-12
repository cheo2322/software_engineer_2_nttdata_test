package com.nttdata.bank.exception;

public class UnavailableEntityException extends RuntimeException {

  public <T> UnavailableEntityException(Class<T> clazz, String identifier) {
    super(
      String.format("Entity of type '%s' with identifier '%s' is unavailable",
        clazz.getSimpleName(),
        identifier)
    );
  }
}
