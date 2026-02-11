package com.nttdata.bank.mapper;

import com.nttdata.bank.dto.ClientDto;
import com.nttdata.bank.entity.Client;
import com.nttdata.bank.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(
  componentModel = "spring"
)
public interface ClientMapper {

  ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

  @Mapping(source = ".", target = "person", qualifiedByName = "dtoToPerson")
  @Mapping(target = "status", ignore = true)
  Client dtoToClient(ClientDto clientDto);

  @Named("dtoToPerson")
  Person dtoToPerson(ClientDto dto);
}
