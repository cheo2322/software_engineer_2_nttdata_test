package com.nttdata.bank.controller;

import com.nttdata.bank.service.MovementService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovementController {

  private final MovementService movementService;

  public MovementController(MovementService movementService) {
    this.movementService = movementService;
  }
}
