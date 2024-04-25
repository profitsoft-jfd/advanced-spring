package dev.profitsoft.fd.springadvanced.service;

import dev.profitsoft.fd.springadvanced.dto.ContractSaveDto;
import dev.profitsoft.fd.springadvanced.dto.PaymentSaveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentGenerator {

  private final PaymentService paymentService;

  private final ContractService contractService;

  public void generateContractsAndPayments(int count) {
    log.info("Start Generating contracts and payments");
    Random rnd = new Random();
    int generated = 0;
    while (generated < count) {
      try {
        String contractNumber = String.valueOf(System.currentTimeMillis() * 10 + rnd.nextInt(10));

        // Create contract. Skip every 10th contract (to simulate different payment statuses)
        if (generated % 10 != 0) {
          contractService.create(buildContract(contractNumber));
        }

        // Create payment
        paymentService.create(buildPayment(contractNumber, rnd));

        generated++;

        if (generated % 100 == 0) {
          log.info("Generated {} contracts and payments", generated);
        }
      } catch (DataIntegrityViolationException e) {
        log.warn("Optimistic lock exception occurred. Retrying...");
      }
    }
    log.info("Finished generating contracts and payments. Totally generated {} records", generated);
  }

  private static PaymentSaveDto buildPayment(String contractNumber, Random rnd) {
    return PaymentSaveDto.builder()
        .sum((double) rnd.nextInt(10000))
        .operationTime(Instant.now())
        .payer("Payer " + rnd.nextInt(100))
        .description("Payment for contract %s".formatted(contractNumber))
        .build();
  }

  private static ContractSaveDto buildContract(String contractNumber) {
    return ContractSaveDto.builder()
        .number(contractNumber)
        .signDate(LocalDate.now())
        .build();
  }

}
