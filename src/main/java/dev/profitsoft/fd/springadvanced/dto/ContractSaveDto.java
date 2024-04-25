package dev.profitsoft.fd.springadvanced.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Getter
@Builder
@Jacksonized
public class ContractSaveDto {

  private String number;

  private LocalDate signDate;

}
