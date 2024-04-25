package dev.profitsoft.fd.springadvanced.controller;

import dev.profitsoft.fd.springadvanced.dto.ContractDetailsDto;
import dev.profitsoft.fd.springadvanced.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

  private final ContractService contractService;

  @GetMapping("/{id}")
  public ContractDetailsDto getContractDetails(@PathVariable("id") String id) {
    return contractService.getContractDetails(id);
  }

}
