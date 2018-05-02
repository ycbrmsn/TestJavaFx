package application.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class ChartHelper {

  /**
   * 创建一个饼图
   * @param title 标题
   * @param names 饼块名
   * @param values 饼大小
   * @return
   */
  public static PieChart createPieChart(String title, List<String> names, List<Double> values) {
    ObservableList<PieChart.Data> data = createPieChartData(names, values);
    return createPie(title, data);
  }
  
  public static ObservableList<PieChart.Data> createPieChartData(List<String> names, List<Double> values) {
    ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
    if (!isIllegal(names, values)) {
      return data;
    }
    for (int i = 0; i < names.size(); i++) {
      data.add(new PieChart.Data(names.get(i), values.get(i)));
    }
    return data;
  }
  
  /**
   * 创建一个饼图
   * @param title 标题
   * @param linkedHashMap 数据
   * @return
   */
  public static PieChart createPieChart(String title, LinkedHashMap<String, Double> linkedHashMap) {
    ObservableList<PieChart.Data> data = createPieChartData(linkedHashMap);
    return createPie(title, data);
  }
  
  public static PieChart createPie(String title, ObservableList<PieChart.Data> data) {
    PieChart pieChart = new PieChart(data);
    pieChart.setTitle(title);
    return pieChart;
  }
  
  public static ObservableList<PieChart.Data> createPieChartData(LinkedHashMap<String, Double> linkedHashMap) {
    ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
    if (data == null) {
      return data;
    }
    Iterator<String> ite = linkedHashMap.keySet().iterator();
    while (ite.hasNext()) {
      String name = ite.next();
      Double value = linkedHashMap.get(name);
      data.add(new PieChart.Data(name, value));
    }
    return data;
  }
  
  /**
   * 创建一个折线图
   * @param title 标题
   * @param names
   * @param linkedHashMapList
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static LineChart createLineChart(String title, List<String> names, 
      List<LinkedHashMap> linkedHashMapList) {
    List<XYChart.Series> series = createLineChartSeries(names, linkedHashMapList);
    return createLineChart(title, series);
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static LineChart createLineChart(String title, List<XYChart.Series> series) {
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    LineChart lineChart = new LineChart<Number, Number>(xAxis, yAxis);
    lineChart.setTitle(title);
    for (XYChart.Series s : series) {
      lineChart.getData().add(s);
    }
    return lineChart;
  }
  
  @SuppressWarnings("rawtypes")
  public static List<XYChart.Series> createLineChartSeries(List<String> names, 
      List<LinkedHashMap> linkedHashMapList) {
    List<XYChart.Series> series = new ArrayList<>();
    if (!isIllegal(names, linkedHashMapList)) {
      return series;
    }
    for (int i = 0; i < names.size(); i++) {
      String name = names.get(i);
      LinkedHashMap linkedHashMap = linkedHashMapList.get(i);
      series.add(createLineChartSer(name, linkedHashMap));
    }
    return series;
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static XYChart.Series createLineChartSer(String name, LinkedHashMap linkedHashMap) {
    XYChart.Series<Number, Number> ser = new XYChart.Series<>();
    if (linkedHashMap == null) {
      return ser;
    }
    if (StringUtils.isNotBlank(name)) {
      ser.setName(name);
    }
    Iterator ite = linkedHashMap.keySet().iterator();
    while (ite.hasNext()) {
      Object xVal = ite.next();
      Object yVal = linkedHashMap.get(xVal);
      ser.getData().add(new XYChart.Data(xVal, yVal));
    }
    return ser;
  }
  
  @SuppressWarnings("rawtypes")
  public static boolean isIllegal(List list1, List list2) {
    return list1 != null && list2 != null && list1.size() == list2.size();
  }
  
  @SuppressWarnings("rawtypes")
  public static boolean isIllegal(List list, Map map) {
    return list != null && map != null && list.size() == map.size();
  }
}
