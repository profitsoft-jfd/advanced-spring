package dev.profitsoft.fd.springadvanced.service;

import dev.profitsoft.fd.springadvanced.data.ContractData;
import dev.profitsoft.fd.springadvanced.dto.ContractDetailsDto;
import dev.profitsoft.fd.springadvanced.dto.ContractSaveDto;
import dev.profitsoft.fd.springadvanced.monitor.Monitored;
import dev.profitsoft.fd.springadvanced.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

  public static final String CACHE_CONTRACT = "CONTRACT";

  private final ContractRepository contractRepository;

  private final CacheManager cacheManager;

  @Monitored
  @Cacheable(value = CACHE_CONTRACT, key = "#number")
  public Optional<ContractData> findContractByNumber(String number) {
    cacheManager.getCache(CACHE_CONTRACT).get(number, ContractData.class);
    Optional<ContractData> result = contractRepository.findByNumber(number);
    log.debug("Searched for Contract by number: {}, found: {}", number, result.isPresent());
    return result;
  }

  public ContractDetailsDto getContractDetails(String id) {
    ContractData data = contractRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Contract with id '%s' not found".formatted(id)));
    return convertToDetailsDto(data);
  }

  private static ContractDetailsDto convertToDetailsDto(ContractData data) {
    return ContractDetailsDto.builder()
        .id(data.getId())
        .number(data.getNumber())
        .signDate(data.getSignDate())
        .payments(data.getPayments().stream()
            .map(PaymentService::convertToDto)
            .toList())
        .build();
  }

  @CacheEvict(CACHE_CONTRACT)
  public String create(ContractSaveDto contractSaveDto) {
    ContractData data = convertToData(contractSaveDto);
    ContractData saved = contractRepository.save(data);
    return saved.getId();
  }

  private static ContractData convertToData(ContractSaveDto contractSaveDto) {
    ContractData result = new ContractData();
    result.setId(UUID.randomUUID().toString());
    result.setNumber(contractSaveDto.getNumber());
    result.setSignDate(LocalDate.now());
    result.setSavedAt(Instant.now());
    return result;
  }

}
