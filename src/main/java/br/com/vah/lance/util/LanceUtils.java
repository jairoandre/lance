package br.com.vah.lance.util;

import br.com.vah.lance.entity.BaseEntity;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.text.SimpleDateFormat;
import java.util.*;

public class LanceUtils {

  public static List<SelectItem> createSelectItem(List<BaseEntity> list, Boolean nullOption) {
    List<SelectItem> selectItems = new ArrayList<>();
    if (nullOption) {
      selectItems.add(new SelectItem(null, "Selecione..."));
    }
    for (BaseEntity entity : list) {
      selectItems.add(new SelectItem(entity.getId(), entity.getLabelForSelectItem()));
    }
    return selectItems;
  }

  public static List<SelectItem> splice(List<SelectItem> items, Object id) {
    List<SelectItem> newItems = new ArrayList<>();
    for (SelectItem item : items) {
      if (!id.equals(item.getValue())) {
        newItems.add(item);
      }
    }
    return newItems;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static List<Map.Entry<? extends Object, ? extends Object>> transformMap(Map<?, ?> map) {
    return new ArrayList(map.entrySet());
  }

  public static String getCurrentDateAsString() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
    return sdf.format(getCalendarForNow());
  }

  public static boolean checkBetween(Date when, Date begin, Date end) {
    if (when == null || begin == null || end == null) {
      throw new IllegalArgumentException("Cannot verify null objects.");
    } else {
      return when.compareTo(begin) >= 0 && when.compareTo(end) <= 0;
    }

  }

  public static boolean checkBetweenDates(Date whenBegin, Date whenEnd, Date begin, Date end) {
    return checkBetween(whenBegin, begin, end) || checkBetween(whenEnd, begin, end);

  }

  public static Date[] getDateRangeForThisMonth() {
    Date begining, end;

    Date[] array = new Date[2];

    {
      Calendar calendar = getCalendarForNow();
      calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
      setTimeToBeginningOfDay(calendar);
      begining = calendar.getTime();
    }

    {
      Calendar calendar = getCalendarForNow();
      calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
      setTimeToEndofDay(calendar);
      end = calendar.getTime();
    }

    array[0] = begining;
    array[1] = end;

    return array;
  }

  private static Calendar getCalendarForNow() {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(new Date());
    return calendar;
  }

  private static void setTimeToBeginningOfDay(Calendar calendar) {
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
  }

  private static void setTimeToEndofDay(Calendar calendar) {
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
  }

  public static void addMsg(FacesMessage msg, boolean flash) {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.addMessage(null, msg);
    if (flash) {
      ctx.getExternalContext().getFlash().setKeepMessages(true);
    }
  }

}
