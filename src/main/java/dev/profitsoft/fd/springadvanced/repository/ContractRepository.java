package dev.profitsoft.fd.springadvanced.repository;

import dev.profitsoft.fd.springadvanced.data.ContractData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ContractRepository extends JpaRepository<ContractData, String> {

  Optional<ContractData> findByNumber(String number);

}
