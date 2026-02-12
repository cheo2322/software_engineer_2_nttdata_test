package com.nttdata.bank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "movements")
@Getter
@Setter
public class Movement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "movement_timestamp", nullable = false, updatable = false)
  private Timestamp timestamp = Timestamp.from(Instant.now());

  @Column(name = "movement_type")
  @Enumerated(EnumType.STRING)
  private MovementType type;

  @Column(name = "movement_value")
  private Double value;
  private Double balance;

  @ManyToOne
  @JoinColumn(name = "account_id", referencedColumnName = "id")
  private Account account;

  public enum MovementType {
    DEPOSIT,
    WITHDRAWAL,
  }
}
