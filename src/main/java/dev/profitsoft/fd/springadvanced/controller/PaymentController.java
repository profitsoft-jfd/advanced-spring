package dev.profitsoft.fd.springadvanced.controller;

import dev.profitsoft.fd.springadvanced.dto.PaymentSaveDto;
import dev.profitsoft.fd.springadvanced.service.PaymentGenerator;
import dev.profitsoft.fd.springadvanced.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for payment operations.
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentGenerator paymentGenerator;

  private final PaymentService paymentService;

  /**
   * Creates a new payment.
   *
   * @param paymentSaveDto payment data
   * @return created payment ID
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String createPayment(@RequestBody PaymentSaveDto paymentSaveDto) {
    return paymentService.create(paymentSaveDto);
  }

  /**
   * Generates test contracts and payments.
   *
   * @param count number of contracts to generate
   */
  @PostMapping("/generate")
  public void generate(@RequestParam int count) {
    paymentGenerator.generateContractsAndPayments(count);
  }

  /**
   * Triggers manual payment processing.
   */
  @PostMapping("/process")
  public void process() {
    paymentService.processPayments();
  }

}
