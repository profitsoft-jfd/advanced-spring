package dev.profitsoft.fd.springadvanced.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

  private ListUtil() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static <T> List<List<T>> splitList(List<T> list, int size) {
    List<List<T>> result = new ArrayList<>();
    for (int i = 0; i < list.size(); i += size) {
      int end = Math.min(list.size(), i + size);
      result.add(new ArrayList<>(list.subList(i, end)));
    }
    return result;
  }

}
