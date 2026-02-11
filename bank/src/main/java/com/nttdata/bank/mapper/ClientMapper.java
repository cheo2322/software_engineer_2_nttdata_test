package com.nttdata.bank.mapper;

import com.nttdata.bank.dto.ClientDto;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.entity.Person;
import com.nttdata.bank.entity.Person.GenrePerson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
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
    if (genre == null) {
      return null;
    }
    try {
      return GenrePerson.valueOf(genre.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid genre: " + genre);
    }
  }
}
