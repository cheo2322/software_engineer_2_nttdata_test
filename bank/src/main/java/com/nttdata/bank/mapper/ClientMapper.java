package com.nttdata.bank.mapper;

import com.nttdata.bank.dto.ClientDto;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.entity.Person;
import com.nttdata.bank.entity.Person.GenrePerson;
import com.nttdata.bank.exception.InvalidFieldException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
  unmappedTargetPolicy = ReportingPolicy.IGNORE,
  unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface ClientMapper {

  ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

  @Mapping(source = ".", target = "person", qualifiedByName = "dtoToPerson")
  @Mapping(target = "status", ignore = true)
  Client dtoToClient(ClientDto clientDto);

  @Named("dtoToPerson")
  @Mapping(source = "genre", target = "genre", qualifiedByName = "stringToGenrePerson")
  Person dtoToPerson(ClientDto dto);

  @Named("stringToGenrePerson")
  default GenrePerson stringToGenrePerson(String genre) {
    try {
      return GenrePerson.valueOf(genre.toUpperCase());
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new InvalidFieldException(Person.class, "genre", genre);
    }
  }

  @Mapping(source = "client.person.name", target = "name")
  @Mapping(source = "client.person.genre", target = "genre")
  @Mapping(source = "client.person.age", target = "age")
  @Mapping(source = "client.person.identification", target = "identification")
  @Mapping(source = "client.person.address", target = "address")
  @Mapping(source = "client.person.phone", target = "phone")
  @Mapping(target = "password", ignore = true)
  ClientDto clientToDto(Client client);
}
