package com.nttdata.bank.repository;

import com.nttdata.bank.entity.Movement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

  Optional<Movement> findTopByAccountIdOrderByTimestampDesc(Long accountId);

  @Query(
    """
        SELECT m
        FROM movements m
        WHERE m.account.id IN :accountIds
        AND m.timestamp BETWEEN :startDate AND :endDate
        ORDER BY m.timestamp ASC
      """
  )
  List<Movement> findAllByAccountsAndDateRange(
    @Param("accountIds") List<Long> accountIds,
    @Param("startDate") Timestamp startDate,
    @Param("endDate") Timestamp endDate);

}
