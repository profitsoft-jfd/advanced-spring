package dev.profitsoft.fd.springadvanced.controller;

import dev.profitsoft.fd.springadvanced.service.PaymentGenerator;
import dev.profitsoft.fd.springadvanced.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentGenerator paymentGenerator;

  private final PaymentService paymentService;

  @PostMapping("/generate")
  public void generate(@RequestParam int count) {
    paymentGenerator.generateContractsAndPayments(count);
  }

  @PostMapping("/process")
  public void process() {
    paymentService.processPayments();
  }

}
