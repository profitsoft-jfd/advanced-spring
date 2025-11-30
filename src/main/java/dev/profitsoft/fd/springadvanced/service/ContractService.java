package dev.profitsoft.fd.springadvanced.service;

import dev.profitsoft.fd.springadvanced.data.ContractData;
import dev.profitsoft.fd.springadvanced.dto.ContractDetailsDto;
import dev.profitsoft.fd.springadvanced.dto.ContractSaveDto;
import dev.profitsoft.fd.springadvanced.monitor.Monitored;
import dev.profitsoft.fd.springadvanced.repository.ContractRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;

/**
 * Service for contract management operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

  public static final String CACHE_CONTRACT = "CONTRACT";

  private final ContractRepository contractRepository;

  /**
   * Finds contract by number with caching.
   *
   * @param number contract number
   * @return optional contract data
   */
  @Monitored
  @Cacheable(value = CACHE_CONTRACT, key = "#number")
  public Optional<ContractData> findContractByNumber(String number) {
    Optional<ContractData> result = contractRepository.findByNumber(number);
    log.debug("Searched for Contract by number: {}, found: {}", number, result.isPresent());
    return result;
  }

  /**
   * Retrieves contract details including payments.
   *
   * @param id contract ID
   * @return contract details
   * @throws IllegalArgumentException if contract not found
   */
  public ContractDetailsDto getContractDetails(String id) {
    ContractData data = contractRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Contract with id '%s' not found".formatted(id)));
    return convertToDetailsDto(data);
  }

  /**
   * Converts contract data to details DTO.
   *
   * @param data contract data
   * @return contract details DTO
   */
  private static ContractDetailsDto convertToDetailsDto(ContractData data) {
    return ContractDetailsDto.builder()
        .id(data.getId())
        .number(data.getNumber())
        .signDate(data.getSignDate())
        .payments(data.getPayments() != null
            ? data.getPayments().stream()
                .map(PaymentService::convertToDto)
                .toList()
            : emptyList())
        .build();
  }

  /**
   * Creates a new contract and evicts cache.
   *
   * @param contractSaveDto contract data
   * @return created contract ID
   */
  @Transactional
  @CacheEvict(value = CACHE_CONTRACT, allEntries = true)
  public String create(ContractSaveDto contractSaveDto) {
    ContractData data = convertToData(contractSaveDto);
    ContractData saved = contractRepository.save(data);
    return saved.getId();
  }

  /**
   * Updates an existing contract and evicts cache.
   *
   * @param id contract ID
   * @param contractSaveDto updated contract data
   * @throws IllegalArgumentException if contract not found
   */
  @Transactional
  @CacheEvict(value = CACHE_CONTRACT, allEntries = true)
  public void update(String id, ContractSaveDto contractSaveDto) {
    ContractData data = contractRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Contract with id '%s' not found".formatted(id)));

    if (contractSaveDto.getNumber() != null) {
      data.setNumber(contractSaveDto.getNumber());
    }
    if (contractSaveDto.getSignDate() != null) {
      data.setSignDate(contractSaveDto.getSignDate());
    }

    contractRepository.save(data);
  }

  /**
   * Converts DTO to contract data entity.
   *
   * @param contractSaveDto contract save DTO
   * @return contract data entity
   */
  private static ContractData convertToData(ContractSaveDto contractSaveDto) {
    ContractData result = new ContractData();
    result.setId(UUID.randomUUID().toString());
    result.setNumber(contractSaveDto.getNumber());
    result.setSignDate(contractSaveDto.getSignDate() != null ? contractSaveDto.getSignDate() : LocalDate.now());
    result.setSavedAt(Instant.now());
    return result;
  }

}
