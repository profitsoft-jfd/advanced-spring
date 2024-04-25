package dev.profitsoft.fd.springadvanced.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentScheduler {

  private final PaymentService paymentService;

  /**
   * Runs at second :00 every 5 minutes starting at minute :00 every hour.
   */
  @Scheduled(cron = "${payment.processing.cron:0 0/5 * * * ?}")
  @SchedulerLock(name = "ProcessPayments", lockAtLeastFor = "PT10S", lockAtMostFor = "PT10M")
  public void processPayments() {
    try {
      paymentService.processPayments();
    } catch (Throwable e) {
      log.error("Error processing payments", e);
    }
  }

}
