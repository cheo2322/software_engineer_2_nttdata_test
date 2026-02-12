package com.nttdata.bank.service;

import com.nttdata.bank.dto.ClientDto;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.entity.Person;
import com.nttdata.bank.mapper.ClientMapper;
import com.nttdata.bank.repository.ClientRepository;
import com.nttdata.bank.util.PasswordUtil;
import org.apache.commons.lang3.StringUtils;
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

  public void updateClient(Long clientId, ClientDto clientDto) {
    Client client = clientRepository.findById(clientId)
      .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

    Person person = client.getPerson();

    if (!StringUtils.isBlank(clientDto.name())) {
      person.setName(clientDto.name());
    }

    if (!StringUtils.isBlank(clientDto.genre())) {
      try {
        person.setGenre(Person.GenrePerson.valueOf(clientDto.genre().toUpperCase()));
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Invalid genre value: " + clientDto.genre());
      }
    }

    if (!StringUtils.isBlank(clientDto.address())) {
      person.setAddress(clientDto.address());
    }

    if (!StringUtils.isBlank(clientDto.phone())) {
      person.setPhone(clientDto.phone());
    }

    client.setPerson(person);

    clientRepository.save(client);
  }

  public void updateClientPassword(Long clientId, String newPassword) {
    Client client = clientRepository.findById(clientId)
      .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

    client.setPasswordHash(PasswordUtil.hashPassword(newPassword));

    clientRepository.save(client);
  }

  public void deleteClient(Long clientId) {
    Client client = clientRepository.findById(clientId)
      .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

    client.setStatus(false);

    clientRepository.save(client);
  }
}
