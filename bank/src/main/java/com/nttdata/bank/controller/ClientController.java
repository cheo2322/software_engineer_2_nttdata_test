package com.nttdata.bank.controller;

import com.nttdata.bank.dto.BankResponse;
import com.nttdata.bank.dto.ClientDto;
import com.nttdata.bank.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank/v1/clients")
public class ClientController {

  private final ClientService clientService;

  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  @PostMapping
  public ResponseEntity<BankResponse<Void>> createClient(@RequestBody ClientDto clientDto) {
    clientService.createClient(clientDto);
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(new BankResponse<>("000", "Client created successfully", null));
  }
}
