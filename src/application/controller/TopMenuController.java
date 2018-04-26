package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.helper.FrameHelper;
import application.service.LoginService;
import application.stage.LoginStage;
import javafx.event.Event;
import javafx.fxml.FXML;

public class TopMenuController extends BaseController {

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // TODO Auto-generated method stub
    
  }
  
  @FXML
  private void logout(Event event) {
    LoginService.logout();
    FrameHelper.closeWindow(FrameHelper.getMainPane());
    LoginStage.show();
  }

}
