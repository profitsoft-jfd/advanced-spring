package dev.profitsoft.fd.springadvanced.dict;

/**
 * Payment processing status.
 */
public enum PaymentStatus {

  /**
   * Payment is newly created and awaiting processing.
   */
  NEW,

  /**
   * Payment has been successfully assigned to a contract.
   */
  ASSIGNED,

  /**
   * Payment could not be assigned to any contract.
   */
  NOT_ASSIGNED

}
