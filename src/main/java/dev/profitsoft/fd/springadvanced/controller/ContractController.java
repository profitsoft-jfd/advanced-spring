package dev.profitsoft.fd.springadvanced.controller;

import dev.profitsoft.fd.springadvanced.dto.ContractDetailsDto;
import dev.profitsoft.fd.springadvanced.dto.ContractSaveDto;
import dev.profitsoft.fd.springadvanced.service.ContractService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for contract operations.
 */
@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
@Tag(name = "Contracts", description = "API for managing contracts")
public class ContractController {

  private final ContractService contractService;

  /**
   * Retrieves contract details by ID.
   *
   * @param id contract ID
   * @return contract details with payments
   */
  @Operation(summary = "Get contract details", description = "Retrieves contract details including associated payments by contract ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Contract found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContractDetailsDto.class))),
      @ApiResponse(responseCode = "404", description = "Contract not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
  })
  @GetMapping("/{id}")
  public ContractDetailsDto getContractDetails(
      @Parameter(description = "Contract ID", required = true) @PathVariable("id") String id) {
    return contractService.getContractDetails(id);
  }

  /**
   * Creates a new contract.
   *
   * @param contractSaveDto contract data
   * @return created contract ID
   */
  @Operation(summary = "Create contract", description = "Creates a new contract with the provided data")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Contract created successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "409", description = "Contract with this number already exists",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String createContract(
      @Parameter(description = "Contract data", required = true) @RequestBody ContractSaveDto contractSaveDto) {
    return contractService.create(contractSaveDto);
  }

  /**
   * Updates an existing contract.
   *
   * @param id contract ID
   * @param contractSaveDto updated contract data
   */
  @Operation(summary = "Update contract", description = "Updates an existing contract with new data")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Contract updated successfully"),
      @ApiResponse(responseCode = "404", description = "Contract not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "409", description = "Contract with this number already exists",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
  })
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateContract(
      @Parameter(description = "Contract ID", required = true) @PathVariable("id") String id,
      @Parameter(description = "Updated contract data", required = true) @RequestBody ContractSaveDto contractSaveDto) {
    contractService.update(id, contractSaveDto);
  }

}
