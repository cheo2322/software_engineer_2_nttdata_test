package com.nttdata.bank.dto;

public record MovementDto(String timestamp, String accountNumber, Double amount, Double balance,
                          String type) {

}
