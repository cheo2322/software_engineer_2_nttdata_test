package com.nttdata.bank.dto;

public record AccountDto(String number, String type, double initialBalance, boolean status,
                         Long clientId) {

}
