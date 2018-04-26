package application.stage;

import application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainStage {
  
  public static Stage show(Stage primaryStage) {
    try {
      if (primaryStage == null) {
        primaryStage = new Stage();
      }
      Parent root = FXMLLoader.load(MainStage.class.getResource("/layout/Main.fxml"));
      Scene scene = new Scene(root, 1000, 700);
      scene.getStylesheets().add(MainStage.class.getResource("/css/application.css").toExternalForm());
      
      primaryStage.setTitle("xxx装修工程报价软件");
      primaryStage.setScene(scene);
      primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/img/new_pro.png")));
      primaryStage.show();
    } catch(Exception e) {
      e.printStackTrace();
    }
    return primaryStage;
  }
  
  public static Stage show() {
    return show(null);
  }
  
}
