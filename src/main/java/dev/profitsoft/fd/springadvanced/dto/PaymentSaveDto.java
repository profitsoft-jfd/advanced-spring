package dev.profitsoft.fd.springadvanced.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

/**
 * DTO for creating a new payment.
 */
@Getter
@Builder
@Jacksonized
public class PaymentSaveDto {

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


}
