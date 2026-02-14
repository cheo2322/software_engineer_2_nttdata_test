package com.nttdata.bank.mapper;

import com.nttdata.bank.dto.AccountDto;
import com.nttdata.bank.entity.Account;
import com.nttdata.bank.entity.Account.AccountType;
import com.nttdata.bank.exception.InvalidFieldException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(
  unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
  unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE
)
public interface AccountMapper {

  AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

  @Mapping(source = "number", target = "accountNumber")
  @Mapping(source = "type", target = "type", qualifiedByName = "mapStringToAccountType")
  @Mapping(target = "client", ignore = true)
  Account dtoToEntity(AccountDto accountDto);

  @Named("mapStringToAccountType")
  default AccountType mapStringToAccountType(String type) {
    try {
      return AccountType.valueOf(type);
    } catch (IllegalArgumentException e) {
      throw new InvalidFieldException(Account.class, "type", type);
    }
  }

  @Mapping(source = "accountNumber", target = "number")
  @Mapping(source = "client.id", target = "clientId")
  @Mapping(source = "client.person.name", target = "clientName")
  AccountDto entityToDto(Account account);
}
