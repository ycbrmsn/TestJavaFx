package application.helper;

import javafx.application.Platform;

public class ActionHelper {

  public static void run(Action backgroundAction, Action uiAction) {
    new Thread() {
      public void run() {
        backgroundAction.onAction();
        Platform.runLater(() -> {
          uiAction.onAction();
        });
      };
    }.start();
  }
  
  public interface Action {
    void onAction();
  }
  
}
