package edu.byu.cs329.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionUtils {
  static final Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

  /**
   * Logs and throws exception if object is null.
   * 
   * @param o object to check for null
   * @param msg message for custom null-pointer exception
   */
  public static <T> void requiresNonNull(final T o, final String msg) {
    if (o != null) {
      return;
    }

    RuntimeException exception = new NullPointerException(msg);
    log.error(msg, exception);
    throw(exception);
  }

  /**
   * Log and throw custom runtime exception.
   * 
   * @param msg msg for custom eception
   */
  public static void throwRuntimeException(final String msg) {
    RuntimeException exception = new RuntimeException(msg);
    log.error(msg, exception);
    throw exception;
  }
}
