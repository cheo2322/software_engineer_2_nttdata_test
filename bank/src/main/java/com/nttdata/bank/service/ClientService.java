package com.nttdata.bank.service;

import com.nttdata.bank.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

  private final ClientRepository clientRepository;

  public ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }
}
