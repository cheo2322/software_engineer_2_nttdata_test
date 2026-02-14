package com.nttdata.bank.controller;

import com.nttdata.bank.dto.BankResponse;
import com.nttdata.bank.dto.MovementDto;
import com.nttdata.bank.service.MovementService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/bank/v1/movements")
public class MovementController {

  private final MovementService movementService;

  public MovementController(MovementService movementService) {
    this.movementService = movementService;
  }

  @PostMapping("/credit")
  public ResponseEntity<BankResponse<MovementDto>> createCredit(
    @RequestBody MovementDto depositDto
  ) {
    MovementDto credit = movementService.createCredit(depositDto);
    return ResponseEntity.ok(new BankResponse<>("000", "Credit created successfully", credit));
  }

  @PostMapping("/debit")
  public ResponseEntity<BankResponse<MovementDto>> createDebit(
    @RequestBody MovementDto withdrawalDto
  ) {
    MovementDto debit = movementService.createDebit(withdrawalDto);
    return ResponseEntity.ok(new BankResponse<>("000", "Debit created successfully", debit));
  }

  @GetMapping
  public ResponseEntity<BankResponse<List<MovementDto>>> getMovements() {
    return ResponseEntity.ok(
      new BankResponse<>(
        "000",
        "Movements retrieved successfully",
        movementService.getAllMovements())
    );
  }
}
