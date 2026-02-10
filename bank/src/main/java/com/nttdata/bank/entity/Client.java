package com.nttdata.bank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "clients")
@Getter
@Setter
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // The same ID works as clientId

  private String passwordHash;
  private ClientStatus status;

  @OneToOne
  @JoinColumn(name = "person_id", referencedColumnName = "id")
  private Person person;

  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
  private List<Account> accounts;

  enum ClientStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED
  }
}
