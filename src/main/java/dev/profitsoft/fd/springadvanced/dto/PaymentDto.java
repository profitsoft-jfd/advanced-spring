package dev.profitsoft.fd.springadvanced.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

/**
 * DTO for payment data.
 */
@Getter
@Builder
@Jacksonized
@Schema(description = "Payment information")
public class PaymentDto {

  /**
   * Payment ID.
   */
  @Schema(description = "Unique payment identifier", example = "550e8400-e29b-41d4-a716-446655440001")
  private String id;

  /**
   * Payment sum.
   */
  @Schema(description = "Payment amount", example = "1500.50")
  private Double sum;

  /**
   * Time when payment was processed.
   */
  @Schema(description = "Timestamp when the payment was processed", example = "2024-01-15T10:30:00Z")
  private Instant operationTime;

  /**
   * Payer name from the bank statement.
   */
  @Schema(description = "Name of the payer from bank statement", example = "John Doe")
  private String payer;

  /**
   * Payment description from the bank statement.
   */
  @Schema(description = "Payment description from bank statement", example = "Payment for contract CNT-2024-001")
  private String description;

}
