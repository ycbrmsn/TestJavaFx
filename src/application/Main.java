package application;
  
import application.stage.LoginStage;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) {
    LoginStage.show(primaryStage);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
