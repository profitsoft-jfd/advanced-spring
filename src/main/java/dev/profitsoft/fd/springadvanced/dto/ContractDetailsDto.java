package dev.profitsoft.fd.springadvanced.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for contract details with payments.
 */
@Getter
@Builder
@Jacksonized
public class ContractDetailsDto {

  /**
   * Contract ID.
   */
  private String id;

  /**
   * Contract number.
   */
  private String number;

  /**
   * Contract sign date.
   */
  private LocalDate signDate;

  /**
   * List of payments associated with the contract.
   */
  private List<PaymentDto> payments;

}
