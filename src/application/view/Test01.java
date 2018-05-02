package application.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test01 {
  public static void main(String[] args) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime dateTime = LocalDateTime.now();
  }
}
