package dev.profitsoft.fd.springadvanced.dto;

import dev.profitsoft.fd.springadvanced.dict.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Result of payment processing operation.
 */
@Getter
@Schema(description = "Result of payment processing operation")
public class PaymentsProcessingResult {

  /**
   * Number of assigned payments.
   */
  @Schema(description = "Number of payments successfully assigned to contracts", example = "15")
  private int assigned;

  /**
   * Number of not assigned payments.
   */
  @Schema(description = "Number of payments that could not be assigned to contracts", example = "3")
  private int notAssigned;

  /**
   * Default constructor.
   */
  public PaymentsProcessingResult() {
  }

  private PaymentsProcessingResult(int assigned, int notAssigned) {
    this.assigned = assigned;
    this.notAssigned = notAssigned;
  }

  /**
   * Creates result based on payment status.
   *
   * @param status payment status
   * @return processing result
   */
  public static PaymentsProcessingResult byStatus(PaymentStatus status) {
    return new PaymentsProcessingResult(
        PaymentStatus.ASSIGNED.equals(status) ? 1 : 0,
        PaymentStatus.NOT_ASSIGNED.equals(status) ? 1 : 0
    );
  }

  /**
   * Appends another result to this one.
   *
   * @param result result to append
   */
  public void append(PaymentsProcessingResult result) {
    this.assigned += result.assigned;
    this.notAssigned += result.notAssigned;
  }

  /**
   * Returns total number of processed payments.
   *
   * @return total processed count
   */
  public int getTotallyProcessed() {
    return assigned + notAssigned;
  }

}
