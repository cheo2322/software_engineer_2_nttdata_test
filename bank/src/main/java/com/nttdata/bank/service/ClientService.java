package com.nttdata.bank.service;

import com.nttdata.bank.dto.ClientDto;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.mapper.ClientMapper;
import com.nttdata.bank.repository.ClientRepository;
import com.nttdata.bank.util.PasswordUtil;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

  private final ClientRepository clientRepository;

  private static final ClientMapper CLIENT_MAPPER = ClientMapper.INSTANCE;

  public ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  public void createClient(ClientDto clientDto) {
    Client client = CLIENT_MAPPER.dtoToClient(clientDto);

    client.setPasswordHash(PasswordUtil.hashPassword(clientDto.password()));
    client.setStatus(true);

    clientRepository.save(client);
  }
}
