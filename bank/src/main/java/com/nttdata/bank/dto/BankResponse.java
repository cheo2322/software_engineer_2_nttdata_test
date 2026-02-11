package com.nttdata.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BankResponse<T> {

  private String code;
  private String message;
  private T data;
}
