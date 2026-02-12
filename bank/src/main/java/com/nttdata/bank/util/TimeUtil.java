package com.nttdata.bank.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

  private TimeUtil() {
  }

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
    .ofPattern("dd-MM-yyyy HH:mm:ss");

  public static Timestamp getTimestampStartOfDay(String date) {
    LocalDateTime startOfDay = LocalDate.parse(date, DATE_FORMATTER).atStartOfDay();
    return Timestamp.valueOf(startOfDay);
  }

  public static Timestamp getTimestampEndOfDay(String date) {
    LocalDateTime endOfDay = LocalDate.parse(date, DATE_FORMATTER).atTime(23, 59, 59);
    return Timestamp.valueOf(endOfDay);
  }

  public static String getDateFromTimestamp(Timestamp timestamp) {
    LocalDateTime localDateTime = timestamp.toLocalDateTime();
    return localDateTime.format(DATE_TIME_FORMATTER);
  }
}
