package com.nttdata.bank.controller;

import com.nttdata.bank.dto.BankResponse;
import com.nttdata.bank.dto.ClientDto;
import com.nttdata.bank.service.ClientService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @PutMapping("/{id}")
  public ResponseEntity<BankResponse<Void>> updateClient(
    @PathVariable("id") Long clientId,
    @RequestBody ClientDto clientDto
  ) {
    clientService.updateClient(clientId, clientDto);
    return ResponseEntity.ok(new BankResponse<>("000", "Client updated successfully", null));
  }

  @PatchMapping("/{id}/password")
  public ResponseEntity<BankResponse<Void>> updateClientPassword(
    @PathVariable("id") Long clientId,
    @RequestParam String newPassword
  ) {
    clientService.updateClientPassword(clientId, newPassword);
    return ResponseEntity.ok(
      new BankResponse<>("000", "Client password updated successfully", null));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BankResponse<Void>> deleteClient(@PathVariable("id") Long clientId) {
    clientService.deleteClient(clientId);
    return ResponseEntity.ok(new BankResponse<>("000", "Client deleted successfully", null));
  }

  @GetMapping
  public ResponseEntity<BankResponse<List<ClientDto>>> getClients() {
    return ResponseEntity.ok(new BankResponse<>(
      "000",
      "Clients retrieved successfully",
      clientService.getAllClients())
    );
  }
}
