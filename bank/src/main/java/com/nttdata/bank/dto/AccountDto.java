package com.nttdata.bank.dto;

public record AccountDto(Long id, String number, String type, double initialBalance, boolean status,
                         Long clientId) {

}
