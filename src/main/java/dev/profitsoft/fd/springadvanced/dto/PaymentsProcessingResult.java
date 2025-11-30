package dev.profitsoft.fd.springadvanced.dto;

import dev.profitsoft.fd.springadvanced.dict.PaymentStatus;
import lombok.Getter;

@Getter
public class PaymentsProcessingResult {

  private int assigned;

  private int notAssigned;

  public PaymentsProcessingResult() {
  }

  private PaymentsProcessingResult(int assigned, int notAssigned) {
    this.assigned = assigned;
    this.notAssigned = notAssigned;
  }

  public static PaymentsProcessingResult byStatus(PaymentStatus status) {
    return new PaymentsProcessingResult(
        PaymentStatus.ASSIGNED.equals(status) ? 1 : 0,
        PaymentStatus.NOT_ASSIGNED.equals(status) ? 1 : 0
    );
  }

  public void append(PaymentsProcessingResult result) {
    this.assigned += result.assigned;
    this.notAssigned += result.notAssigned;
  }

  public int getTotallyProcessed() {
    return assigned + notAssigned;
  }

}
