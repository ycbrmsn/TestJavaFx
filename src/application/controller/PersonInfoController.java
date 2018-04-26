package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.helper.ActionHelper;
import application.service.PersonService;
import application.vo.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PersonInfoController extends BaseController {
  
  @FXML
  private Label nameLabel;
  @FXML
  private Label genderLabel;
  @FXML
  private Label ageLabel;
  
  private Person person;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initViews();
  }

  private void initViews() {
    ActionHelper.run(() -> {
      Integer id = getParameter("id");
      person = PersonService.getPersonById(id);
    }, () -> {
      if (person != null) {
        nameLabel.setText(person.getName());
        genderLabel.setText(person.getGender());
        ageLabel.setText(String.valueOf(person.getAge()));
      }
    });
    
  }
  
}
