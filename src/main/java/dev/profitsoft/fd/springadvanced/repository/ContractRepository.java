package dev.profitsoft.fd.springadvanced.repository;

import dev.profitsoft.fd.springadvanced.data.ContractData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository for contract data access.
 */
@Repository
public interface ContractRepository extends JpaRepository<ContractData, String> {

  /**
   * Finds contract by number.
   *
   * @param number contract number
   * @return optional contract data
   */
  Optional<ContractData> findByNumber(String number);

}
