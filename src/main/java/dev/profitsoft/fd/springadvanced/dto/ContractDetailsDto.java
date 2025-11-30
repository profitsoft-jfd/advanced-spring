package dev.profitsoft.fd.springadvanced.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Contract details with associated payments")
public class ContractDetailsDto {

  /**
   * Contract ID.
   */
  @Schema(description = "Unique contract identifier", example = "550e8400-e29b-41d4-a716-446655440000")
  private String id;

  /**
   * Contract number.
   */
  @Schema(description = "Contract number", example = "CNT-2024-001")
  private String number;

  /**
   * Contract sign date.
   */
  @Schema(description = "Date when the contract was signed", example = "2024-01-15")
  private LocalDate signDate;

  /**
   * List of payments associated with the contract.
   */
  @Schema(description = "List of payments associated with this contract")
  private List<PaymentDto> payments;

}
