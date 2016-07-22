package br.com.vah.lance.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple date formatting utility class
 * @author Emre Simtay <emre@simtay.com>
 */

public class DateUtility {

	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	public static String getCurrentDateTime() {
    DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    Date date = new Date();
    return dateFormat.format(date);
	}

  public static Date[] monthRange(Date data) {
    Calendar cld = Calendar.getInstance();
    cld.setTime(data);
    cld.set(Calendar.DAY_OF_MONTH, 1);
    cld.set(Calendar.HOUR, 0);
    cld.set(Calendar.MINUTE, 0);
    cld.set(Calendar.SECOND, 0);
    Date begin = cld.getTime();
    cld.add(Calendar.MONTH, 1);
    cld.add(Calendar.SECOND, -1);
    Date end = cld.getTime();
    return new Date[] {begin, end};
  }


}