package application.stage;
  
import application.helper.FrameHelper;
import application.helper.Point;
import application.helper.Rect;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;


public class LoadingStage {
  
  private static final double LOADING_WIDTH = 50;
  private static final double LOADING_HEIGHT = 50;
  
  public static Stage show(Stage primaryStage, Window owner) {
    try {
      if (primaryStage == null) {
        primaryStage = new Stage();
      }
      ProgressIndicator pi = new ProgressIndicator(-1);
      Scene scene = new Scene(pi);
      scene.getStylesheets().add(LoadingStage.class.getResource("/css/application.css").toExternalForm());
      
      primaryStage.initStyle(StageStyle.TRANSPARENT);
      primaryStage.initModality(Modality.WINDOW_MODAL);
      primaryStage.initOwner(owner);
      Point point = FrameHelper.getModalPoint(owner, new Rect(LOADING_WIDTH, LOADING_HEIGHT));
      primaryStage.setX(point.getX());
      primaryStage.setY(point.getY());
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch(Exception e) {
      e.printStackTrace();
    }
    return primaryStage;
  }
  
  public static Stage show(Event event) {
    return show(null, FrameHelper.getWindow(event));
  }
  
  public static Stage show() {
    return show(null);
  }
  
}
