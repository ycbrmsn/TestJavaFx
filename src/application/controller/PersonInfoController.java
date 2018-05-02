package application.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;

import application.helper.ActionHelper;
import application.helper.ChartHelper;
import application.service.PersonService;
import application.vo.Person;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class PersonInfoController extends BaseController {
  
  @FXML
  private ScrollPane scrollPane;
  @FXML
  private VBox personVBox;
  @FXML
  private Label personTitleLabel;
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
    scrollPane.setFitToWidth(true);
    personTitleLabel.prefWidthProperty().bind(personVBox.widthProperty());
    personVBox.getChildren().add(createPieChart());
    personVBox.getChildren().add(createLineChart());
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
  
  private PieChart createPieChart() {
    LinkedHashMap<String, Double> linkedHashMap = new LinkedHashMap<>();
    for (int i = 0; i < 10; i++) {
      linkedHashMap.put("块" + (i + 1), Double.valueOf((i + 2)));
    }
    return ChartHelper.createPieChart("一个饼", linkedHashMap);
  }
  
  @SuppressWarnings("rawtypes")
  private LineChart createLineChart() {
    List<String> names = new ArrayList<>();
    List<LinkedHashMap> linkedHashMapList = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      names.add("这是线" + (i + 1));
      LinkedHashMap<Number, Number> linkedHashMap = new LinkedHashMap<>();
      for (int j = 0; j < 10; j++) {
        linkedHashMap.put(j + 1, (i * 10 + j + Math.random()) * 10);
      }
      linkedHashMapList.add(linkedHashMap);
    }
    return ChartHelper.createLineChart("一条线", names, linkedHashMapList);
  }
  
}
