package com.nttdata.bank.dto;

public record Report(String date, String client, String accountNumber, String type,
                     double initialBalance, boolean status, double movement,
                     double availableBalance) {

}
