package application.stage;
  
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class LoginStage {
  
  public static Stage show(Stage primaryStage) {
    try {
      if (primaryStage == null) {
        primaryStage = new Stage();
      }
      Parent root = FXMLLoader.load(LoginStage.class.getResource("/layout/Login.fxml"));
      Scene scene = new Scene(root, 600, 400);
      scene.getStylesheets().add(LoginStage.class.getResource("/css/application.css").toExternalForm());
      
      primaryStage.setTitle("用户登录");
      primaryStage.setScene(scene);
      primaryStage.getIcons().add(new Image(LoginStage.class.getResourceAsStream("/img/new_pro.png")));
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
