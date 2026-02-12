package com.nttdata.bank.repository;

import com.nttdata.bank.entity.Client;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

  Optional<Client> findByPersonIdentification(String identification);
}
