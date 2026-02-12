package com.nttdata.bank.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "accounts")
@Getter
@Setter
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "account_number", unique = true)
  private String accountNumber;

  @Column(name = "account_type")
  @Enumerated(EnumType.STRING)
  private AccountType type;

  @Column(name = "initial_balance")
  private Double initialBalance;

  @Column(name = "account_status")
  private Boolean status;

  @ManyToOne
  @JoinColumn(name = "client_id", referencedColumnName = "id")
  private Client client;

  @OneToMany(mappedBy = "account", cascade = CascadeType.PERSIST)
  private List<Movement> movements = new ArrayList<>();

  public enum AccountType {
    SAVINGS,
    CHECKING,
  }
}
