package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.helper.FrameHelper;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class MainController extends BaseController {
  
  @FXML
  private BorderPane mainPane;
  
  @FXML
  private SplitPane centerPane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    FrameHelper.setMainPane(mainPane);
    FrameHelper.setCenterPane(centerPane);
    TabPane rightPane = new TabPane();
    FrameHelper.setRightPane(rightPane);
    long t0 = new java.util.Date().getTime();
    mainPane.setTop(FrameHelper.loadView("TopMenu"));
    long t1 = new java.util.Date().getTime();
    centerPane.getItems().add(FrameHelper.loadView("LeftMenu"));
    centerPane.getItems().add(rightPane);
    centerPane.setDividerPosition(0, 0.25);
    long t2 = new java.util.Date().getTime();
    System.out.println("init topMenu: " + (t1 - t0));
    System.out.println("init leftMenu: " + (t2 - t1));
  }
}
