package dev.profitsoft.fd.springadvanced.service;

import dev.profitsoft.fd.springadvanced.data.PaymentData;
import dev.profitsoft.fd.springadvanced.dict.PaymentStatus;
import dev.profitsoft.fd.springadvanced.dto.ContractSaveDto;
import dev.profitsoft.fd.springadvanced.dto.PaymentSaveDto;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PaymentServiceTest {

  @Autowired
  ContractService contractService;

  @Autowired
  PaymentService paymentService;

  @Autowired
  EntityManager entityManager;

  @Test
  @Transactional
  public void processPayments() {
    contractService.create(ContractSaveDto.builder()
        .number("111")
        .signDate(LocalDate.now())
        .build());

    String payment1Id = paymentService.create(PaymentSaveDto.builder()
        .sum(100.0)
        .operationTime(Instant.now())
        .payer("Payer 1")
        .description("Payment for contract 111")
        .build());

    String payment2Id = paymentService.create(PaymentSaveDto.builder()
        .sum(200.0)
        .operationTime(Instant.now())
        .payer("Payer 2")
        .description("Payment for contract 222")
        .build());

    paymentService.processPayments();

    PaymentData payment1 = entityManager.find(PaymentData.class, payment1Id);
    assertThat(payment1.getStatus()).isEqualTo(PaymentStatus.ASSIGNED);
    assertThat(payment1.getContractNumber()).isEqualTo("111");
    assertThat(payment1.getContract()).isNotNull();
    assertThat(payment1.getContract().getNumber()).isEqualTo("111");

    PaymentData payment2 = entityManager.find(PaymentData.class, payment2Id);
    assertThat(payment2.getStatus()).isEqualTo(PaymentStatus.NOT_ASSIGNED);
    assertThat(payment2.getContractNumber()).isEqualTo("222");
    assertThat(payment2.getContract()).isNull();
  }

}
