package com.nttdata.bank.exception;

public class EntityNotFoundException extends RuntimeException {

  public <T> EntityNotFoundException(Class<T> clazz, String id) {
    super(String.format("%s with id %s not found", clazz.getSimpleName(), id));
  }
}
