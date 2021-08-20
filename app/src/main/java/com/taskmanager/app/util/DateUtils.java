package com.taskmanager.app.util;

import com.taskmanager.app.exception.InvalidMonthException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class DateUtils {
  private DateUtils() {}

  public static Integer getYearFromCurrentDate() {
    return Calendar.getInstance().get(Calendar.YEAR);
  }

  public static Integer getYearFromDate(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.get(Calendar.YEAR);
  }

  public static Integer getMonthFromDate(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.get(Calendar.MONTH) + 1;
  }

  public static String getStringDate(Date date) {
    return getStringDate(date, "dd/MM/yyyy");
  }

  public static String getStringDate(Date date, String format) {
    try {
      DateFormat dateFormat = new SimpleDateFormat(format);
      return dateFormat.format(date);
    } catch (Exception e) {
      return null;
    }
  }

  public static Integer getDaysFromDate(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  public static Integer getDaysFromCurrentDate() {
    return getDaysFromDate(new Date());
  }

  public static Integer getMaxDaysFromDate(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  public static Boolean isLastDateOfMonth(Date date) {
    int days = getDaysFromDate(date);
    int maxDaysOfMonth = getMaxDaysFromDate(date);
    return days == maxDaysOfMonth;
  }

  public static Boolean isFutureMonthYear(Integer billMonth, Integer billYear) {
    Date firstDateOfMonthYear = getDatefromString("1/" + billMonth + "/" + billYear, "dd/MM/yyyy");
    Date currentDate = getDatefromString(getStringDate(new Date(), "dd/MM/yyyy"), "dd/MM/yyyy");
    Map<String, Integer> firstDateMonthYear = getMonthYear(firstDateOfMonthYear, 0);
    Map<String, Integer> currentDateMonthYear = getMonthYear(currentDate, 0);
    return firstDateOfMonthYear.after(Objects.requireNonNull(currentDate))
        && (!firstDateMonthYear.get("month").equals(currentDateMonthYear.get("month"))
        || !firstDateMonthYear.get("year").equals(currentDateMonthYear.get("year")));
  }

  public static String getShortMonthNameFromNumericValue(String fullMonthName)
      throws InvalidMonthException {
    Integer numericValue = getNumaricValueOfMonth(fullMonthName);
    return getShortMonthNameFromNumericValue(numericValue);
  }

  public static String getShortMonthNameFromNumericValue(Integer numericValue)
      throws InvalidMonthException {
    if (numericValue <= 12 && numericValue > 0) {
      return new DateFormatSymbols().getShortMonths()[numericValue - 1];
    }
    throw new InvalidMonthException("Invalid month");
  }

  public static Map<String, Integer> getPreviousMonthYear(Date date) {
    return getMonthYear(date, -1);
  }

  public static Map<String, Integer> getNextMonthYear(Date date) {
    return getMonthYear(date, 1);
  }

  private static Map<String, Integer> getMonthYear(Date date, int addMonthNum) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MONTH, addMonthNum);
    int month = cal.get(Calendar.MONTH) + 1;
    int year = cal.get(Calendar.YEAR);
    Map<String, Integer> resultMap = new HashMap<>();
    resultMap.put("month", month);
    resultMap.put("year", year);
    return resultMap;
  }

  public static long calculateDays(Date startDate, Date endDate) {
    long diff = endDate.getTime() - startDate.getTime();
    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
  }

  public static Date getPreviousWeekDate() {
    Date date = new Date();
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
    c.add(Calendar.DATE, -i - 7);
    return c.getTime();
  }

  public static Integer getMonthFromCurrentDate() {
    return Calendar.getInstance().get(Calendar.MONTH) + 1;
  }

  public static Long getDaysBetweenTwoDate(Date startDate, Date endDate) {
    long diffInMillies = Math.abs(startDate.getTime() - endDate.getTime());
    return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
  }

  public static Date addDays(Date date, int days) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DAY_OF_MONTH, days); // minus number would decrement the days
    return cal.getTime();
  }

  public static Date addDays(String date, int days) {
    return addDays(date, "dd/MM/yyyy", days);
  }

  public static Date addDays(String sdate, String format, int days) {
    Date date = getDatefromString(sdate, format);
    return addDays(date, days);
  }

  public static Date getDatefromString(String date) {
    return getDatefromString(date, "dd/MM/yyyy");
  }

  public static Date getDatefromString(String date, String format) {
    if (date == null || date.equals("")) {
      return null;
    }
    DateFormat dateFormat = new SimpleDateFormat(format);
    try {
      return dateFormat.parse(date);
    } catch (ParseException e) {
      return null;
    }
  }

  public static Integer getNumaricValueOfMonth(String monthName) throws InvalidMonthException {
    Date date;
    try {
      date = new SimpleDateFormat("MMMM").parse(monthName);
    } catch (ParseException e) {
      throw new InvalidMonthException("Invalid month");
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    System.out.println(cal.get(Calendar.MONTH));
    return cal.get(Calendar.MONTH) + 1;
  }

  public static Date calculateExpiryDate(int expiryTimeInMinutes) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Timestamp(cal.getTime().getTime()));
    cal.add(Calendar.MINUTE, expiryTimeInMinutes);
    return new Date(cal.getTime().getTime());
  }

  public static Date getExpirationTime(Long expireHours) {
    Date now = new Date();
    long expireInMilis = TimeUnit.HOURS.toMillis(expireHours);
    return new Date(expireInMilis + now.getTime());
  }
}
