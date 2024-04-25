package dev.profitsoft.fd.springadvanced.dto;

import dev.profitsoft.fd.springadvanced.data.PaymentData;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@Jacksonized
public class ContractDetailsDto {

  private String id;

  /**
   * Contract number.
   */
  private String number;

  private LocalDate signDate;

  private List<PaymentDto> payments;

}
