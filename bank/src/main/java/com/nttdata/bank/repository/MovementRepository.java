package com.nttdata.bank.repository;

import com.nttdata.bank.entity.Movement;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends CrudRepository<Movement, Long> {

  Optional<Movement> findTopByAccountIdOrderByTimestampDesc(Long accountId);
}
