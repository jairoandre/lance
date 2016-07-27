package br.com.vah.lance.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jairoportela on 19/07/2016.
 */
public class ArquivoUtils {

  public static String leftZeros(String value, int size) {
    return symbol(value, "0", size, false);
  }

  public static String leftZeros (Object value, int size) {
    return symbol(value.toString(), "0", size, false);
  }

  public static String rightSpace(String value, int size) {
    return symbol(value, " ", size, true);
  }

  public static String formatNumber(BigDecimal valor, int size) {
    DecimalFormat df = new DecimalFormat("#,##0.00");
    String str = df.format(valor);
    str = str.replace(",", "").replace(".", "");
    return leftZeros(str, size);
  }

  public static String formatDate(Date date, String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.format(date);
  }

  public static String rightSpace(Object value, int size) {
    return symbol(value.toString(), " ", size, true);
  }

  public static String symbol(String value, String symbol, int size, boolean right) {
    StringBuilder buffer = new StringBuilder();

    if (value == null) {
      value = "";
    }

    if (value.length() > size) {
      return value.substring(0, size);
    }
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

  public static void main(String... args) {
    BigDecimal bd = new BigDecimal(250.32);

    System.out.println(formatNumber(bd, 13));
  }
}
