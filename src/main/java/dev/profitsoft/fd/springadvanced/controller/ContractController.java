package dev.profitsoft.fd.springadvanced.controller;

import dev.profitsoft.fd.springadvanced.dto.ContractDetailsDto;
import dev.profitsoft.fd.springadvanced.dto.ContractSaveDto;
import dev.profitsoft.fd.springadvanced.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
public class ContractController {

  private final ContractService contractService;

  /**
   * Retrieves contract details by ID.
   *
   * @param id contract ID
   * @return contract details with payments
   */
  @GetMapping("/{id}")
  public ContractDetailsDto getContractDetails(@PathVariable("id") String id) {
    return contractService.getContractDetails(id);
  }

  /**
   * Creates a new contract.
   *
   * @param contractSaveDto contract data
   * @return created contract ID
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String createContract(@RequestBody ContractSaveDto contractSaveDto) {
    return contractService.create(contractSaveDto);
  }

  /**
   * Updates an existing contract.
   *
   * @param id contract ID
   * @param contractSaveDto updated contract data
   */
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateContract(@PathVariable("id") String id,
                              @RequestBody ContractSaveDto contractSaveDto) {
    contractService.update(id, contractSaveDto);
  }

}
