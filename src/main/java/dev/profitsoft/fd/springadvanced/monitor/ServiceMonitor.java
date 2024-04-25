package dev.profitsoft.fd.springadvanced.monitor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
public class ServiceMonitor {

  @Around("@annotation(Monitored)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    log.debug("Method '{}' with params={} executed in {} ms",
        joinPoint.getSignature().toShortString(),
        List.of(joinPoint.getArgs()),
        System.currentTimeMillis() - start);
    return result;
  }

}
