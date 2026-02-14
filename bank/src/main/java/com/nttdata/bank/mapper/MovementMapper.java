package com.nttdata.bank.mapper;

import com.nttdata.bank.dto.MovementDto;
import com.nttdata.bank.entity.Movement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(
  unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
  unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE
)
public interface MovementMapper {

  MovementMapper INSTANCE = Mappers.getMapper(MovementMapper.class);

  @Mapping(source = "account.accountNumber", target = "accountNumber")
  @Mapping(source = "value", target = "amount")
  MovementDto entityToDto(Movement movement);
}
