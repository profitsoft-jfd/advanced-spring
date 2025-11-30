package dev.profitsoft.fd.springadvanced.repository;

import dev.profitsoft.fd.springadvanced.data.PaymentData;
import dev.profitsoft.fd.springadvanced.dict.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for payment data access.
 */
@Repository
public interface PaymentRepository extends JpaRepository<PaymentData, String> {

  /**
   * Finds all payment IDs for the given payment status.
   *
   * @param status payment status
   * @return list of payment IDs
   */
  @Query("SELECT id FROM PaymentData WHERE status = ?1")
  List<String> findIdsByStatus(PaymentStatus status);

}
