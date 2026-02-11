package com.nttdata.bank.controller;

import com.nttdata.bank.service.ClientService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

  private final ClientService clientService;

  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }
}
