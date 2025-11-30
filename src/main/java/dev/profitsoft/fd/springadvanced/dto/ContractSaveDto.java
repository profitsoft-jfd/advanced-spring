package dev.profitsoft.fd.springadvanced.dto;

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
public class ContractSaveDto {

  /**
   * Contract number.
   */
  private String number;

  /**
   * Contract sign date.
   */
  private LocalDate signDate;

}
