package dev.profitsoft.fd.springadvanced.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

/**
 * DTO for creating a new contract.
 */
@Getter
@Builder
@Jacksonized
@Schema(description = "Data for creating or updating a contract")
public class ContractSaveDto {

  /**
   * Contract number.
   */
  @Schema(description = "Unique contract number", example = "CNT-2024-001", required = true)
  private String number;

  /**
   * Contract sign date.
   */
  @Schema(description = "Date when the contract was signed", example = "2024-01-15", required = true)
  private LocalDate signDate;

}
