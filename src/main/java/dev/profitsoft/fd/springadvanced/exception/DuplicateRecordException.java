package dev.profitsoft.fd.springadvanced.exception;

/**
 * Exception thrown when attempting to create a record with a duplicate unique field.
 */
public class DuplicateRecordException extends RuntimeException {

  /**
   * Creates a new exception with the specified message.
   *
   * @param message the detail message
   */
  public DuplicateRecordException(String message) {
    super(message);
  }

  /**
   * Creates a new exception with the specified message and cause.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public DuplicateRecordException(String message, Throwable cause) {
    super(message, cause);
  }

}
