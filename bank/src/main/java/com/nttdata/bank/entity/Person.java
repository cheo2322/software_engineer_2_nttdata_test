package com.nttdata.bank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "persons")
@Getter
@Setter
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "person_name")
  private String name;

  @Enumerated(EnumType.STRING)
  private GenrePerson genre;
  private Integer age;

  @Column(unique = true)
  private String identification;

  private String address;

  @Column(unique = true)
  private String phone;

  public enum GenrePerson {
    MALE,
    FEMALE,
    OTHER
  }
}
