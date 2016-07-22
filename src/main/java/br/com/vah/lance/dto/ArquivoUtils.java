package br.com.vah.lance.dto;

/**
 * Created by jairoportela on 19/07/2016.
 */
public class ArquivoUtils {

  public static String leftZeros(String value, int size) {
    return symbol(value, "0", size, false);
  }

  public static String rightSpace(String value, int size) {
    return symbol(value, " ", size, true);
  }

  public static String symbol(String value, String symbol, int size, boolean right) {
    StringBuilder buffer = new StringBuilder();
    if (right) {
      buffer.append(value);
    }
    for (int i = 0; i < size - value.length(); i++) {
      buffer.append(symbol);
    }
    if (!right) {
      buffer.append(value);
    }
    return buffer.toString();
  }
}
