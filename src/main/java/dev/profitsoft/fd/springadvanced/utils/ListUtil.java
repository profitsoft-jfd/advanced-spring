package dev.profitsoft.fd.springadvanced.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for list operations.
 */
public class ListUtil {

  private ListUtil() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  /**
   * Splits a list into batches of specified size.
   *
   * @param list source list
   * @param size batch size
   * @param <T> list element type
   * @return list of batches
   */
  public static <T> List<List<T>> splitList(List<T> list, int size) {
    List<List<T>> result = new ArrayList<>();
    for (int i = 0; i < list.size(); i += size) {
      int end = Math.min(list.size(), i + size);
      result.add(new ArrayList<>(list.subList(i, end)));
    }
    return result;
  }

}
