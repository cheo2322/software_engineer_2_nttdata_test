package com.nttdata.bank.repository;

import com.nttdata.bank.entity.Movement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends CrudRepository<Movement, Long> {

}
