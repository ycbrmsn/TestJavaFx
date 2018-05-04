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
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.StackedBarChart;
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
    personVBox.getChildren().addAll(createPieChart(), createLineChart(), createAreaChart(), 
        createStackedAreaChart(), createBarChart1(), createBarChart2(), createStackedBarChart1(),
        createStackedBarChart2(), createBubbleChart(), createScatterChart());
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
    return ChartHelper.createLineChart("一条线", names, linkedHashMapList, "横线", "竖线");
  }
  
  @SuppressWarnings("rawtypes")
  private AreaChart createAreaChart() {
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
    return ChartHelper.createAreaChart("区域", names, linkedHashMapList, "横线", "竖线");
  }
  
  @SuppressWarnings("rawtypes")
  private StackedAreaChart createStackedAreaChart() {
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
    return ChartHelper.createStackedAreaChart("区域", names, linkedHashMapList, "横线", "竖线");
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  private BarChart createBarChart1() {
    List<String> names = new ArrayList<>();
    List<LinkedHashMap> linkedHashMapList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      names.add("这是柱子" + (i + 1));
      LinkedHashMap linkedHashMap = new LinkedHashMap<>();
      for (int j = 0; j < 5; j++) {
        linkedHashMap.put("柱子" + (j + 1), (i * 10 + j + Math.random()) * 10);
      }
      linkedHashMapList.add(linkedHashMap);
    }
    return ChartHelper.createBarChart("许多柱子", names, linkedHashMapList, "横柱子", "竖柱子");
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  private BarChart createBarChart2() {
    List<String> names = new ArrayList<>();
    List<LinkedHashMap> linkedHashMapList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      names.add("这是柱子" + (i + 1));
      LinkedHashMap linkedHashMap = new LinkedHashMap<>();
      for (int j = 0; j < 5; j++) {
        linkedHashMap.put((i * 10 + j + Math.random()) * 10, "柱子" + (j + 1));
      }
      linkedHashMapList.add(linkedHashMap);
    }
    return ChartHelper.createBarChart("许多柱子", names, linkedHashMapList, "横柱子", "竖柱子");
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  private StackedBarChart createStackedBarChart1() {
    List<String> names = new ArrayList<>();
    List<LinkedHashMap> linkedHashMapList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      names.add("这是柱子" + (i + 1));
      LinkedHashMap linkedHashMap = new LinkedHashMap<>();
      for (int j = 0; j < 5; j++) {
        linkedHashMap.put("柱子" + (j + 1), (i * 10 + j + Math.random()) * 10);
      }
      linkedHashMapList.add(linkedHashMap);
    }
    return ChartHelper.createStackedBarChart("许多柱子", names, linkedHashMapList, "横柱子", "竖柱子");
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  private StackedBarChart createStackedBarChart2() {
    List<String> names = new ArrayList<>();
    List<LinkedHashMap> linkedHashMapList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      names.add("这是柱子" + (i + 1));
      LinkedHashMap linkedHashMap = new LinkedHashMap<>();
      for (int j = 0; j < 5; j++) {
        linkedHashMap.put((i * 10 + j + Math.random()) * 10, "柱子" + (j + 1));
      }
      linkedHashMapList.add(linkedHashMap);
    }
    return ChartHelper.createStackedBarChart("许多柱子", names, linkedHashMapList, "横柱子", "竖柱子");
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  private BubbleChart createBubbleChart() {
    List<String> names = new ArrayList<>();
    List<LinkedHashMap> linkedHashMapList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      names.add("这是气泡" + (i + 1));
      LinkedHashMap linkedHashMap = new LinkedHashMap<>();
      for (int j = 0; j < 5; j++) {
        linkedHashMap.put((j + 1), (i * 10 + j + Math.random()));
      }
      linkedHashMapList.add(linkedHashMap);
    }
    return ChartHelper.createBubbleChart("许多气泡", names, linkedHashMapList, "横泡", "竖泡");
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  private ScatterChart createScatterChart() {
    List<String> names = new ArrayList<>();
    List<LinkedHashMap> linkedHashMapList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      names.add("这是离散" + (i + 1));
      LinkedHashMap linkedHashMap = new LinkedHashMap<>();
      for (int j = 0; j < 5; j++) {
        linkedHashMap.put(j + Math.random() * 5, (i + j + Math.random() * 10));
      }
      linkedHashMapList.add(linkedHashMap);
    }
    return ChartHelper.createScatterChart("离散图", names, linkedHashMapList, "横散", "竖散");
  }
  
}
