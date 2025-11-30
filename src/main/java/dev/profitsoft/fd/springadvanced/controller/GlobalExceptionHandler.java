package dev.profitsoft.fd.springadvanced.controller;

import dev.profitsoft.fd.springadvanced.exception.DuplicateRecordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST controllers.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles IllegalArgumentException and returns 404 Not Found.
   *
   * @param ex the exception
   * @return problem detail with error information
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
    log.warn("Resource not found: {}", ex.getMessage());
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.NOT_FOUND,
        ex.getMessage()
    );
    problemDetail.setTitle("Resource Not Found");
    return problemDetail;
  }

  /**
   * Handles DuplicateRecordException and returns 409 Conflict.
   *
   * @param ex the exception
   * @return problem detail with error information
   */
  @ExceptionHandler(DuplicateRecordException.class)
  public ProblemDetail handleDuplicateRecordException(DuplicateRecordException ex) {
    log.warn("Duplicate record: {}", ex.getMessage());
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.CONFLICT,
        ex.getMessage()
    );
    problemDetail.setTitle("Duplicate Record");
    return problemDetail;
  }

}
