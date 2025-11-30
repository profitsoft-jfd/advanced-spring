package dev.profitsoft.fd.springadvanced.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.profitsoft.fd.springadvanced.dto.PaymentSaveDto;
import dev.profitsoft.fd.springadvanced.repository.PaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for PaymentController endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PaymentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PaymentRepository paymentRepository;

  @AfterEach
  void tearDown() {
    paymentRepository.deleteAll();
  }

  @Test
  void createPayment_shouldReturnCreatedId() throws Exception {
    PaymentSaveDto request = PaymentSaveDto.builder()
        .sum(1500.50)
        .operationTime(Instant.parse("2025-11-30T10:15:30Z"))
        .payer("John Doe")
        .description("Payment for contract CONTRACT-2025-001")
        .build();

    mockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$", notNullValue()));
  }

  @Test
  void createPayment_withMinimalData_shouldReturnCreatedId() throws Exception {
    PaymentSaveDto request = PaymentSaveDto.builder()
        .sum(250.00)
        .operationTime(Instant.parse("2025-11-30T14:30:00Z"))
        .payer("Jane Smith")
        .build();

    mockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$", notNullValue()));
  }

  @Test
  void createPayment_withFullData_shouldReturnCreatedId() throws Exception {
    PaymentSaveDto request = PaymentSaveDto.builder()
        .sum(3750.75)
        .operationTime(Instant.parse("2025-11-29T08:00:00Z"))
        .payer("Company ABC Ltd.")
        .description("Payment for contract CONTRACT-2025-999 - invoice #12345")
        .build();

    mockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$", notNullValue()));
  }

}
