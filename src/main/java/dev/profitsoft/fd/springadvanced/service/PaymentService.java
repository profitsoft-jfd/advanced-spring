package dev.profitsoft.fd.springadvanced.service;

import dev.profitsoft.fd.springadvanced.data.ContractData;
import dev.profitsoft.fd.springadvanced.data.PaymentData;
import dev.profitsoft.fd.springadvanced.dict.PaymentStatus;
import dev.profitsoft.fd.springadvanced.dto.PaymentDto;
import dev.profitsoft.fd.springadvanced.dto.PaymentSaveDto;
import dev.profitsoft.fd.springadvanced.dto.PaymentsProcessingResult;
import dev.profitsoft.fd.springadvanced.monitor.Monitored;
import dev.profitsoft.fd.springadvanced.repository.PaymentRepository;
import dev.profitsoft.fd.springadvanced.utils.ListUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for payment processing operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

  private static final int PAYMENT_BATCH_SIZE = 200;

  private static Pattern CONTRACT_NUMBER_PATTERN = Pattern.compile("Payment for contract (\\S+)");

  private final ContractService contractService;

  private final PaymentRepository paymentRepository;

  private final TransactionTemplate transactionTemplate;

  /**
   * Creates a new payment.
   *
   * @param paymentSaveDto payment data
   * @return created payment ID
   */
  @Transactional
  public String create(PaymentSaveDto paymentSaveDto) {
    PaymentData data = convertToData(paymentSaveDto);
    PaymentData saved = paymentRepository.save(data);
    return saved.getId();
  }

  /**
   * Processes all new payments in batches, assigning them to contracts.
   */
  @Monitored
  public void processPayments() {
    log.info("Start processing payments");
    List<String> allPaymentIds = paymentRepository.findIdsByStatus(PaymentStatus.NEW);
    log.info("Found {} new payments to be processed", allPaymentIds.size());
    PaymentsProcessingResult result = new PaymentsProcessingResult();
    List<List<String>> batches = ListUtil.splitList(allPaymentIds, PAYMENT_BATCH_SIZE);
    for (List<String> paymentIds : batches) {
      result.append(processPaymentsByIds(paymentIds));
      log.info("Processed {} payments from {}", result.getTotallyProcessed(), allPaymentIds.size());
    }
    log.info("Finished processing {} payments. Assigned {}, not assigned {}",
        result.getTotallyProcessed(), result.getAssigned(), result.getNotAssigned());
  }

  /**
   * Processes a batch of payments by IDs.
   * Uses TransactionTemplate to ensure atomicity of the batch operation.
   *
   * @param paymentIds list of payment IDs
   * @return processing result
   */
  private PaymentsProcessingResult processPaymentsByIds(List<String> paymentIds) {
    return transactionTemplate.execute(status -> {
      PaymentsProcessingResult result = new PaymentsProcessingResult();
      List<PaymentData> payments = paymentRepository.findAllById(paymentIds);
      for (PaymentData payment : payments) {
        result.append(processPayment(payment));
      }
      paymentRepository.saveAll(payments);
      return result;
    });
  }

  /**
   * Processes a single payment, extracting contract number and assigning to contract.
   *
   * @param payment payment data
   * @return processing result
   */
  private PaymentsProcessingResult processPayment(PaymentData payment) {
    Optional<String> contractNumberOpt = extractNumberFromDescription(payment.getDescription());
    if (contractNumberOpt.isPresent()) {
      String contractNumber = contractNumberOpt.get();
      payment.setContractNumber(contractNumber);
      Optional<ContractData> contractOpt = contractService.findContractByNumber(contractNumber);
      if (contractOpt.isPresent()) {
        payment.setContract(contractOpt.get());
        payment.setStatus(PaymentStatus.ASSIGNED);
        log.debug("Payment {} assigned to contract {}", payment.getId(), contractNumber);
      } else {
        log.debug("Cannot find contract with number {}", contractNumber);
        payment.setStatus(PaymentStatus.NOT_ASSIGNED);
      }
    } else {
      log.debug("Cannot find contract number in payment description: {}", payment.getDescription());
      payment.setStatus(PaymentStatus.NOT_ASSIGNED);
    }
    return PaymentsProcessingResult.byStatus(payment.getStatus());
  }

  /**
   * Extracts contract number from payment description using regex.
   *
   * @param description payment description
   * @return optional contract number
   */
  private Optional<String> extractNumberFromDescription(String description) {
    if (description == null) {
      return Optional.empty();
    }
    Matcher matcher = CONTRACT_NUMBER_PATTERN.matcher(description);
    if (matcher.find()) {
      return Optional.of(matcher.group(1));
    }
    return Optional.empty();
  }

  /**
   * Converts DTO to payment data entity.
   *
   * @param paymentSaveDto payment save DTO
   * @return payment data entity
   */
  private PaymentData convertToData(PaymentSaveDto paymentSaveDto) {
    PaymentData result = new PaymentData();
    result.setId(UUID.randomUUID().toString());
    result.setSum(paymentSaveDto.getSum());
    result.setOperationTime(paymentSaveDto.getOperationTime());
    result.setPayer(paymentSaveDto.getPayer());
    result.setDescription(paymentSaveDto.getDescription());
    result.setStatus(PaymentStatus.NEW);
    result.setSavedAt(Instant.now());
    return result;
  }

  /**
   * Converts payment data to DTO.
   *
   * @param data payment data
   * @return payment DTO
   */
  public static PaymentDto convertToDto(PaymentData data) {
    return PaymentDto.builder()
        .id(data.getId())
        .sum(data.getSum())
        .operationTime(data.getOperationTime())
        .payer(data.getPayer())
        .description(data.getDescription())
        .build();
  }

}
