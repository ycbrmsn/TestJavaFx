package application.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.util.StringConverter;

public class DateTimeHelper {

  public static StringConverter<LocalDate> instanceDateConverter(String pattern, Locale locale) {
    StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern, locale);

      @Override
      public String toString(LocalDate date) {
        if (date != null) {
          return dateFormatter.format(date);
        } else {
          return "";
        }
      }

      @Override
      public LocalDate fromString(String string) {
        if (string != null && !string.isEmpty()) {
          return LocalDate.parse(string, dateFormatter);
        } else {
          return null;
        }
      }
    };
    return converter;
  }
  
  public static StringConverter<LocalDate> instanceDateConverter(String pattern) {
    StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

      @Override
      public String toString(LocalDate date) {
        if (date != null) {
          return dateFormatter.format(date);
        } else {
          return "";
        }
      }

      @Override
      public LocalDate fromString(String string) {
        if (string != null && !string.isEmpty()) {
          return LocalDate.parse(string, dateFormatter);
        } else {
          return null;
        }
      }
    };
    return converter;
  }
  
  public static StringConverter<LocalDateTime> instanceDateTimeConverTer(String pattern, Locale locale) {
    StringConverter<LocalDateTime> converter = new StringConverter<LocalDateTime>() {
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern, locale);

      @Override
      public String toString(LocalDateTime date) {
        if (date != null) {
          return dateFormatter.format(date);
        } else {
          return "";
        }
      }

      @Override
      public LocalDateTime fromString(String string) {
        if (string != null && !string.isEmpty()) {
          return LocalDateTime.parse(string, dateFormatter);
        } else {
          return null;
        }
      }
    };
    return converter;
  }
  
  public static StringConverter<LocalDateTime> instanceDateTimeConverTer(String pattern) {
    StringConverter<LocalDateTime> converter = new StringConverter<LocalDateTime>() {
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

      @Override
      public String toString(LocalDateTime date) {
        if (date != null) {
          return dateFormatter.format(date);
        } else {
          return "";
        }
      }

      @Override
      public LocalDateTime fromString(String string) {
        if (string != null && !string.isEmpty()) {
          return LocalDateTime.parse(string, dateFormatter);
        } else {
          return null;
        }
      }
    };
    return converter;
  }
}
