package com.nttdata.bank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "movements")
@Getter
@Setter
public class Movement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Timestamp timestamp;
  private MovementType movementType;
  private Double value;
  private Double balance;

  enum MovementType {
    DEPOSIT,
    WITHDRAWAL,
  }
}
