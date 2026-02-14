package com.nttdata.bank.dto;

public record ClientDto(Long id, String name, String genre, Integer age, String identification,
                        String address, String phone, String password, Boolean status) {

}
