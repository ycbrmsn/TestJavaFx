package application.view;

import application.stage.LoginStage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TestMain extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    try {
      Parent root = FXMLLoader.load(TestMain.class.getResource("/application/view/Test.fxml"));
      Scene scene = new Scene(root, 600, 400);
      scene.getStylesheets().add(TestMain.class.getResource("/css/application.css").toExternalForm());
      
//      primaryStage.setTitle("用户登录");
      primaryStage.setScene(scene);
      primaryStage.getIcons().add(new Image(TestMain.class.getResourceAsStream("/img/new_pro.png")));
      primaryStage.show();
    } catch(Exception e) {
      e.printStackTrace();
    }
    
  }

  public static void main(String[] args) {
    launch(args);
  }
}
