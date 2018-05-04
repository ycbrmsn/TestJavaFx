package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import application.helper.FrameHelper;
import application.service.LoginService;
import application.stage.LoginStage;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;

public class TopMenuController extends BaseController {
  
  @FXML
  private Button button1;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Glyph glyph = new Glyph("FontAwesome", FontAwesome.Glyph.SPINNER);
    glyph.size(30);
    button1.setGraphic(glyph);
    button1.setContentDisplay(ContentDisplay.TOP);
  }
  
  @FXML
  private void logout(Event event) {
    LoginService.logout();
    FrameHelper.closeWindow(FrameHelper.getMainPane());
    LoginStage.show();
  }

}
