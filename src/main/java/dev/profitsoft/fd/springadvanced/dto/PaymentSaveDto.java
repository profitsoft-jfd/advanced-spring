package dev.profitsoft.fd.springadvanced.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Data for creating a new payment")
public class PaymentSaveDto {

  /**
   * Payment sum.
   */
  @Schema(description = "Payment amount", example = "1500.50", required = true)
  private Double sum;

  /**
   * Time when payment was processed.
   */
  @Schema(description = "Timestamp when the payment was processed", example = "2024-01-15T10:30:00Z", required = true)
  private Instant operationTime;

  /**
   * Payer name from the bank statement.
   */
  @Schema(description = "Name of the payer from bank statement", example = "John Doe", required = true)
  private String payer;

  /**
   * Payment description from the bank statement.
   */
  @Schema(description = "Payment description from bank statement", example = "Payment for contract CNT-2024-001", required = true)
  private String description;


}
