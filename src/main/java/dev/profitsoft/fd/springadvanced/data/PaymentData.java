package dev.profitsoft.fd.springadvanced.data;

import dev.profitsoft.fd.springadvanced.dict.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Payment persistent entity.
 */
@Getter
@Setter
@Entity
@Table(name = "payment")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PaymentData {

  @Id
  @EqualsAndHashCode.Include
  private String id;

  /**
   * Payment sum.
   */
  private Double sum;

  /**
   * Time when payment was processed.
   */
  private Instant operationTime;

  /**
   * Payer name from the bank statement.
   */
  private String payer;

  /**
   * Payment description from the bank statement.
   */
  private String description;

  @Enumerated(EnumType.STRING)
  private PaymentStatus status;

  /**
   * Contract number extracted from the payment description.
   */
  private String contractNumber;

  /**
   * Contract to which the payment is related.
   * Being set later when the contract is found by the contract number.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_contract")
  private ContractData contract;

  private Instant savedAt;

}
