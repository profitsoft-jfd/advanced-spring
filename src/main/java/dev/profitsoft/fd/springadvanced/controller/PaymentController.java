package dev.profitsoft.fd.springadvanced.controller;

import dev.profitsoft.fd.springadvanced.dto.PaymentSaveDto;
import dev.profitsoft.fd.springadvanced.service.PaymentGenerator;
import dev.profitsoft.fd.springadvanced.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
@Tag(name = "Payments", description = "API for managing payments and payment processing")
public class PaymentController {

  private final PaymentGenerator paymentGenerator;

  private final PaymentService paymentService;

  /**
   * Creates a new payment.
   *
   * @param paymentSaveDto payment data
   * @return created payment ID
   */
  @Operation(summary = "Create payment", description = "Creates a new payment record")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Payment created successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String createPayment(
      @Parameter(description = "Payment data", required = true) @RequestBody PaymentSaveDto paymentSaveDto) {
    return paymentService.create(paymentSaveDto);
  }

  /**
   * Generates test contracts and payments.
   *
   * @param count number of contracts to generate
   */
  @Operation(summary = "Generate test data", description = "Generates test contracts and payments for testing purposes")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Test data generated successfully")
  })
  @PostMapping("/generate")
  public void generate(
      @Parameter(description = "Number of contracts to generate", required = true) @RequestParam int count) {
    paymentGenerator.generateContractsAndPayments(count);
  }

  /**
   * Triggers manual payment processing.
   */
  @Operation(summary = "Process payments", description = "Triggers manual processing of unprocessed payments to assign them to contracts")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Payment processing completed")
  })
  @PostMapping("/process")
  public void process() {
    paymentService.processPayments();
  }

}
