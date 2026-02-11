package com.nttdata.bank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

  @Column(name = "movement_timestamp")
  private Timestamp timestamp;

  @Column(name = "movement_type")
  private MovementType type;

  @Column(name = "movement_value")
  private Double value;
  private Double balance;

  @OneToOne
  @JoinColumn(name = "account_id", referencedColumnName = "id")
  private Account account;

  enum MovementType {
    DEPOSIT,
    WITHDRAWAL,
  }
}
