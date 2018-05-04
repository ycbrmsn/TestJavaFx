package application.view;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import application.view.mydatepicker.control.MyDateTimePicker;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class TestMain extends Application implements Initializable {

  @FXML
  private MyDateTimePicker myDateTimePicker;

  @Override
  public void start(Stage primaryStage) throws Exception {
    try {
      Parent root = FXMLLoader.load(TestMain.class.getResource("/application/view/Test.fxml"));
      Scene scene = new Scene(root, 600, 400);
      scene.getStylesheets().add(TestMain.class.getResource("/css/application.css").toExternalForm());

      // primaryStage.setTitle("用户登录");
      primaryStage.setScene(scene);
      primaryStage.getIcons().add(new Image(TestMain.class.getResourceAsStream("/img/new_pro.png")));
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    StringConverter<LocalDateTime> converter = new StringConverter<LocalDateTime>() {
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
    myDateTimePicker.setConverter(converter);
    myDateTimePicker.setShowTime(false);
  }
}
