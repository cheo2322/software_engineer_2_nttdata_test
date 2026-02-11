package com.nttdata.bank.service;

import com.nttdata.bank.repository.MovementRepository;
import org.springframework.stereotype.Service;

@Service
public class MovementService {

  private final MovementRepository movementRepository;

  public MovementService(MovementRepository movementRepository) {
    this.movementRepository = movementRepository;
  }
}
