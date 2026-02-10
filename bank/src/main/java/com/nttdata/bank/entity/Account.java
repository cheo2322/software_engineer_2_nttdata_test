package com.nttdata.bank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "accounts")
@Getter
@Setter
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String accountNumber;
  private AccountType accountType;
  private Double initialBalance;
  private AccountStatus status;

  @ManyToOne
  @JoinColumn(name = "client_id", referencedColumnName = "id")
  private Client client;

  enum AccountType {
    SAVINGS,
    CHECKING,

  }

  enum AccountStatus {
    ACTIVE,
    INACTIVE,
    CLOSED,
    BLOCKED
  }
}
