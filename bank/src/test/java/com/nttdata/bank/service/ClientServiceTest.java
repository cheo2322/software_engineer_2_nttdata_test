package com.nttdata.bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nttdata.bank.dto.ClientDto;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.entity.Person;
import com.nttdata.bank.exception.EntityNotFoundException;
import com.nttdata.bank.exception.InvalidFieldException;
import com.nttdata.bank.repository.ClientRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

  @Mock
  private ClientRepository clientRepository;

  @InjectMocks
  private ClientService clientService;

  private ClientDto clientDto;
  private Client client;

  @BeforeEach
  void setUp() {
    clientDto = new ClientDto("John Doe", "MALE", 30, "123456",
      "Main Street", "555-1234", "secret", "true");

    Person person = new Person();
    person.setName("John Doe");
    person.setGenre(Person.GenrePerson.MALE);
    person.setAddress("Main Street");
    person.setPhone("555-1234");

    client = new Client();
    client.setId(1L);
    client.setPerson(person);
  }

  @Test
  void testCreateClient() {
    clientService.createClient(clientDto);

    verify(clientRepository, times(1)).save(any(Client.class));
  }

  @Test
  void testUpdateClient_success() {
    when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

    clientService.updateClient(1L, clientDto);

    verify(clientRepository, times(1)).save(client);
    assertEquals("John Doe", client.getPerson().getName());
    assertEquals(Person.GenrePerson.MALE, client.getPerson().getGenre());
  }

  @Test
  void testUpdateClient_notFound() {
    when(clientRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
      () -> clientService.updateClient(99L, clientDto));

    verify(clientRepository, never()).save(any());
  }

  @Test
  void testUpdateClient_updateNameOnly() {
    when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

    ClientDto dto = new ClientDto("New Name", null, null, null,
      null, null, null, null);

    clientService.updateClient(1L, dto);

    assertEquals("New Name", client.getPerson().getName());
    verify(clientRepository).save(client);
  }

  @Test
  void testUpdateClient_updateGenreOnly() {
    when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

    ClientDto dto = new ClientDto(null, "female", null, null,
      null, null, null, null);

    clientService.updateClient(1L, dto);

    assertEquals(Person.GenrePerson.FEMALE, client.getPerson().getGenre());
    verify(clientRepository).save(client);
  }

  @Test
  void testUpdateClient_updateAddressOnly() {
    when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

    ClientDto dto = new ClientDto(null, null, null, null,
      "New Address", null, null, null);

    clientService.updateClient(1L, dto);

    assertEquals("New Address", client.getPerson().getAddress());
    verify(clientRepository).save(client);
  }

  @Test
  void testUpdateClient_updatePhoneOnly() {
    when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

    ClientDto dto = new ClientDto(null, null, null, null,
      null, "999-8888", null, null);

    clientService.updateClient(1L, dto);

    assertEquals("999-8888", client.getPerson().getPhone());
    verify(clientRepository).save(client);
  }

  @Test
  void testUpdateClient_invalidGenre() {
    when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

    ClientDto invalidDto = new ClientDto("Jane", "INVALID", 25, "999",
      "Street", "555-9999", "pwd", "true");

    assertThrows(InvalidFieldException.class,
      () -> clientService.updateClient(1L, invalidDto));

    verify(clientRepository, never()).save(any());
  }

  @Test
  void testUpdateClientPassword_success() {
    when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

    clientService.updateClientPassword(1L, "newSecret");

    verify(clientRepository, times(1)).save(client);
    assertNotNull(client.getPasswordHash());
  }

  @Test
  void testUpdateClientPassword_notFound() {
    when(clientRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
      () -> clientService.updateClientPassword(99L, "newSecret"));

    verify(clientRepository, never()).save(any());
  }

  @Test
  void testDeleteClient_success() {
    when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

    clientService.deleteClient(1L);

    verify(clientRepository, times(1)).save(client);
    assertFalse(client.getStatus());
  }

  @Test
  void testDeleteClient_notFound() {
    when(clientRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
      () -> clientService.deleteClient(99L));

    verify(clientRepository, never()).save(any());
  }
}