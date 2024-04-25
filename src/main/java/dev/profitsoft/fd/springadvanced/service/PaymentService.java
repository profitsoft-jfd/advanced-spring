package dev.profitsoft.fd.springadvanced.service;

import dev.profitsoft.fd.springadvanced.data.ContractData;
import dev.profitsoft.fd.springadvanced.data.PaymentData;
import dev.profitsoft.fd.springadvanced.data.PaymentStatus;
import dev.profitsoft.fd.springadvanced.dto.PaymentDto;
import dev.profitsoft.fd.springadvanced.dto.PaymentSaveDto;
import dev.profitsoft.fd.springadvanced.dto.PaymentsProcessingResult;
import dev.profitsoft.fd.springadvanced.monitor.Monitored;
import dev.profitsoft.fd.springadvanced.repository.PaymentRepository;
import dev.profitsoft.fd.springadvanced.utils.ListUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

  private static final int PAYMENT_BATCH_SIZE = 200;

  private static Pattern CONTRACT_NUMBER_PATTERN = Pattern.compile("Payment for contract (\\S+)");

  private final ContractService contractService;

  private final PaymentRepository paymentRepository;

  public String create(PaymentSaveDto paymentSaveDto) {
    PaymentData data = convertToData(paymentSaveDto);
    PaymentData saved = paymentRepository.save(data);
    return saved.getId();
  }

  @Monitored
  public void processPayments() {
    log.info("Start processing payments");
    List<String> allPaymentIds = paymentRepository.findIdsByStatus(PaymentStatus.NEW);
    log.info("Found {} new payments to be processed", allPaymentIds.size());
    PaymentsProcessingResult result = new PaymentsProcessingResult();
    for (List<String> paymentIds : ListUtil.splitList(allPaymentIds, PAYMENT_BATCH_SIZE)) {
      result.append(processPaymentsByIds(paymentIds));
      log.info("Processed {} payments from {}", result.getTotallyProcessed(), allPaymentIds.size());
    }
    log.info("Finished processing {} payments. Assigned {}, not assigned {}",
        result.getTotallyProcessed(), result.getAssigned(), result.getNotAssigned());
  }

  private PaymentsProcessingResult processPaymentsByIds(List<String> paymentIds) {
    PaymentsProcessingResult result = new PaymentsProcessingResult();
    List<PaymentData> payments = paymentRepository.findAllById(paymentIds);
    for (PaymentData payment : payments) {
      result.append(processPayment(payment));
    }
    paymentRepository.saveAll(payments);
    return result;
  }

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
