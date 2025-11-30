package dev.profitsoft.fd.springadvanced.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.profitsoft.fd.springadvanced.dto.ContractSaveDto;
import dev.profitsoft.fd.springadvanced.service.ContractService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for ContractController endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ContractControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ContractService contractService;

  @Test
  void createContract_shouldReturnCreatedId() throws Exception {
    ContractSaveDto request = ContractSaveDto.builder()
        .number("TEST-001")
        .signDate(LocalDate.of(2025, 11, 30))
        .build();

    mockMvc.perform(post("/api/contracts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$", notNullValue()));
  }

  @Test
  void createContract_withoutSignDate_shouldUseCurrentDate() throws Exception {
    ContractSaveDto request = ContractSaveDto.builder()
        .number("TEST-002")
        .build();

    mockMvc.perform(post("/api/contracts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$", notNullValue()));
  }

  @Test
  void getContractById_shouldReturnContractDetails() throws Exception {
    // Create a contract first
    String contractId = contractService.create(ContractSaveDto.builder()
        .number("TEST-003")
        .signDate(LocalDate.of(2025, 11, 30))
        .build());

    mockMvc.perform(get("/api/contracts/{id}", contractId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(contractId))
        .andExpect(jsonPath("$.number").value("TEST-003"))
        .andExpect(jsonPath("$.signDate").value("2025-11-30"))
        .andExpect(jsonPath("$.payments").isArray());
  }

  @Test
  void getContractById_withNonExistentId_shouldReturnNotFound() throws Exception {
    mockMvc.perform(get("/api/contracts/{id}", "non-existent-id"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.title").value("Resource Not Found"))
        .andExpect(jsonPath("$.detail").exists());
  }

  @Test
  void updateContract_shouldReturnNoContent() throws Exception {
    // Create a contract first
    String contractId = contractService.create(ContractSaveDto.builder()
        .number("TEST-005")
        .signDate(LocalDate.of(2025, 11, 30))
        .build());

    ContractSaveDto updateRequest = ContractSaveDto.builder()
        .number("TEST-005-UPDATED")
        .signDate(LocalDate.of(2025, 12, 1))
        .build();

    mockMvc.perform(put("/api/contracts/{id}", contractId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNoContent());

    // Verify the update by fetching the contract
    mockMvc.perform(get("/api/contracts/{id}", contractId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(contractId))
        .andExpect(jsonPath("$.number").value("TEST-005-UPDATED"))
        .andExpect(jsonPath("$.signDate").value("2025-12-01"));
  }

  @Test
  void updateContract_partialUpdate_shouldUpdateOnlyProvidedFields() throws Exception {
    // Create a contract first
    String contractId = contractService.create(ContractSaveDto.builder()
        .number("TEST-006")
        .signDate(LocalDate.of(2025, 11, 30))
        .build());

    // Update only the number
    ContractSaveDto updateRequest = ContractSaveDto.builder()
        .number("TEST-006-UPDATED")
        .build();

    mockMvc.perform(put("/api/contracts/{id}", contractId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNoContent());

    // Verify only number was updated
    mockMvc.perform(get("/api/contracts/{id}", contractId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(contractId))
        .andExpect(jsonPath("$.number").value("TEST-006-UPDATED"))
        .andExpect(jsonPath("$.signDate").value("2025-11-30"));
  }

  @Test
  void updateContract_withNonExistentId_shouldReturnNotFound() throws Exception {
    ContractSaveDto updateRequest = ContractSaveDto.builder()
        .number("TEST-007")
        .build();

    mockMvc.perform(put("/api/contracts/{id}", "non-existent-id")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.title").value("Resource Not Found"))
        .andExpect(jsonPath("$.detail").exists());
  }

  @Test
  void getContractById_withPayments_shouldIncludePaymentsList() throws Exception {
    // Create a contract
    String contractId = contractService.create(ContractSaveDto.builder()
        .number("TEST-008")
        .signDate(LocalDate.of(2025, 11, 30))
        .build());

    // Get contract details - should include empty payments array
    mockMvc.perform(get("/api/contracts/{id}", contractId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(contractId))
        .andExpect(jsonPath("$.number").value("TEST-008"))
        .andExpect(jsonPath("$.payments").isArray())
        .andExpect(jsonPath("$.payments", hasSize(0)));
  }

}
