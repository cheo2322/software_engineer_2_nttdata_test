package com.nttdata.bank.repository;

import com.nttdata.bank.entity.Account;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

  Optional<Account> findByAccountNumber(String accountNumber);

}
