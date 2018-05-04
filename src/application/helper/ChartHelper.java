package application.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.StackedBarChart;
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
   * 创建一幅饼图
   * @param title 标题
   * @param linkedHashMap 数据
   * @return
   */
  public static PieChart createPieChart(String title, LinkedHashMap<String, Double> linkedHashMap) {
    ObservableList<PieChart.Data> data = createPieChartData(linkedHashMap);
    return createPie(title, data);
  }
  
  public static PieChart createPie(String title, ObservableList<PieChart.Data> data) {
    PieChart chart = new PieChart(data);
    chart.setTitle(title);
    return chart;
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
   * 创建一幅折线图
   * @param title 标题
   * @param names 折线名称
   * @param linkedHashMapList 数据
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static LineChart<Number, Number> createLineChart(String title, List<String> names, 
      List<LinkedHashMap> linkedHashMapList, String xLabel, String yLabel) {
    List<XYChart.Series> series = createLineChartSeries(names, linkedHashMapList);
    return createLineChart(title, series, xLabel, yLabel);
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static LineChart<Number, Number> createLineChart(String title, List<XYChart.Series> series, String xLabel, String yLabel) {
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    LineChart<Number, Number> chart = new LineChart<Number, Number>(xAxis, yAxis);
    chart.setTitle(title);
    if (StringUtils.isNotBlank(xLabel)) {
      xAxis.setLabel(xLabel);
    }
    if (StringUtils.isNotBlank(yLabel)) {
      yAxis.setLabel(yLabel);
    }
    for (XYChart.Series<Number, Number> s : series) {
      chart.getData().add(s);
    }
    return chart;
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
    XYChart.Series ser = new XYChart.Series<>();
    if (linkedHashMap == null) {
      return ser;
    }
    if (StringUtils.isNotBlank(name)) {
      ser.setName(name);
    }
    Iterator<Number> ite = linkedHashMap.keySet().iterator();
    while (ite.hasNext()) {
      Object xVal = ite.next();
      Object yVal = linkedHashMap.get(xVal);
      ser.getData().add(new XYChart.Data(xVal, yVal));
    }
    return ser;
  }
  
  /**
   * 创建一幅区域图
   * @param title 标题
   * @param names 区域名称
   * @param linkedHashMapList 数据
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static AreaChart<Number, Number> createAreaChart(String title, List<String> names, 
      List<LinkedHashMap> linkedHashMapList, String xLabel, String yLabel) {
    List<XYChart.Series> series = createLineChartSeries(names, linkedHashMapList);
    return createAreaChart(title, series, xLabel, yLabel);
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static AreaChart<Number, Number> createAreaChart(String title, List<XYChart.Series> series, String xLabel, String yLabel) {
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    AreaChart<Number, Number> chart = new AreaChart<Number, Number>(xAxis, yAxis);
    chart.setTitle(title);
    if (StringUtils.isNotBlank(xLabel)) {
      xAxis.setLabel(xLabel);
    }
    if (StringUtils.isNotBlank(yLabel)) {
      yAxis.setLabel(yLabel);
    }
    for (XYChart.Series s : series) {
      chart.getData().add(s);
    }
    return chart;
  }
  
  /**
   * 创建一幅栈形区域图
   * @param title 标题
   * @param names 区域名称
   * @param linkedHashMapList 数据
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static StackedAreaChart<Number, Number> createStackedAreaChart(String title, List<String> names, 
      List<LinkedHashMap> linkedHashMapList, String xLabel, String yLabel) {
    List<XYChart.Series> series = createLineChartSeries(names, linkedHashMapList);
    return createStackedAreaChart(title, series, xLabel, yLabel);
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static StackedAreaChart<Number, Number> createStackedAreaChart(String title, List<XYChart.Series> series, String xLabel, String yLabel) {
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    StackedAreaChart<Number, Number> chart = new StackedAreaChart<Number, Number>(xAxis, yAxis);
    chart.setTitle(title);
    if (StringUtils.isNotBlank(xLabel)) {
      xAxis.setLabel(xLabel);
    }
    if (StringUtils.isNotBlank(yLabel)) {
      yAxis.setLabel(yLabel);
    }
    for (XYChart.Series s : series) {
      chart.getData().add(s);
    }
    return chart;
  }
  
  /**
   * 创建一幅柱状图
   * @param title 标题
   * @param names 柱子名称
   * @param linkedHashMapList 数据
   * @param xLabel 横轴名称
   * @param yLabel 纵轴名称
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static BarChart createBarChart(String title, List<String> names, 
      List<LinkedHashMap> linkedHashMapList, String xLabel, String yLabel) {
    List<XYChart.Series> series = createLineChartSeries(names, linkedHashMapList);
    return createBarChart(title, series, xLabel, yLabel);
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static BarChart createBarChart(String title, List<XYChart.Series> series, String xLabel, String yLabel) {
    BarChart chart;
    List<Axis> list = createAxisBySeries(series);
    chart = new BarChart(list.get(0), list.get(1));
    chart.setTitle(title);
    if (StringUtils.isNotBlank(xLabel)) {
      chart.getXAxis().setLabel(xLabel);
    }
    if (StringUtils.isNotBlank(yLabel)) {
      chart.getYAxis().setLabel(yLabel);
    }
    for (XYChart.Series s : series) {
      chart.getData().add(s);
    }
    return chart;
  }
  
  /**
   * 创建一幅栈形柱状图
   * @param title 标题
   * @param names 柱子名称
   * @param linkedHashMapList 数据
   * @param xLabel 横轴名称
   * @param yLabel 纵轴名称
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static StackedBarChart createStackedBarChart(String title, List<String> names, 
      List<LinkedHashMap> linkedHashMapList, String xLabel, String yLabel) {
    List<XYChart.Series> series = createLineChartSeries(names, linkedHashMapList);
    return createStackedBarChart(title, series, xLabel, yLabel);
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static StackedBarChart createStackedBarChart(String title, List<XYChart.Series> series, String xLabel, String yLabel) {
    StackedBarChart chart;
    List<Axis> list = createAxisBySeries(series);
    chart = new StackedBarChart(list.get(0), list.get(1));
    chart.setTitle(title);
    if (StringUtils.isNotBlank(xLabel)) {
      chart.getXAxis().setLabel(xLabel);
    }
    if (StringUtils.isNotBlank(yLabel)) {
      chart.getYAxis().setLabel(yLabel);
    }
    for (XYChart.Series s : series) {
      chart.getData().add(s);
    }
    return chart;
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static List<Axis> createAxisBySeries(List<XYChart.Series> series) {
    List<Axis> list = new ArrayList<>();
    Axis xAxis, yAxis;
    if (series.size() > 0) {
      XYChart.Series s = series.get(0);
      ObservableList<XYChart.Data> data = s.getData();
      if (data.size() > 0) {
        XYChart.Data d = data.get(0);
        Object xValue = d.getXValue();
        if (xValue instanceof String) {
          xAxis = new CategoryAxis();
          yAxis = new NumberAxis();
        } else {
          xAxis = new NumberAxis();
          yAxis = new CategoryAxis();
        }
      } else {
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
      }
    } else {
      xAxis = new CategoryAxis();
      yAxis = new NumberAxis();
    }
    list.add(xAxis);
    list.add(yAxis);
    return list;
  }
  
  /**
   * 创建一幅气泡图
   * @param title 标题
   * @param names 名称
   * @param linkedHashMapList 数据
   * @param xLabel 横轴名称
   * @param yLabel 纵轴名称
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static BubbleChart createBubbleChart(String title, List<String> names, 
      List<LinkedHashMap> linkedHashMapList, String xLabel, String yLabel) {
    List<XYChart.Series> series = createLineChartSeries(names, linkedHashMapList);
    return createBubbleChart(title, series, xLabel, yLabel);
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static BubbleChart createBubbleChart(String title, List<XYChart.Series> series, String xLabel, String yLabel) {
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    BubbleChart chart = new BubbleChart(xAxis, yAxis);
    chart.setTitle(title);
    if (StringUtils.isNotBlank(xLabel)) {
      chart.getXAxis().setLabel(xLabel);
    }
    if (StringUtils.isNotBlank(yLabel)) {
      chart.getYAxis().setLabel(yLabel);
    }
    for (XYChart.Series s : series) {
      chart.getData().add(s);
    }
    return chart;
  }
  
  @SuppressWarnings("rawtypes")
  public static ScatterChart createScatterChart(String title, List<String> names, 
      List<LinkedHashMap> linkedHashMapList, String xLabel, String yLabel) {
    List<XYChart.Series> series = createLineChartSeries(names, linkedHashMapList);
    return createScatterChart(title, series, xLabel, yLabel);
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static ScatterChart createScatterChart(String title, List<XYChart.Series> series, String xLabel, String yLabel) {
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    ScatterChart chart = new ScatterChart(xAxis, yAxis);
    chart.setTitle(title);
    if (StringUtils.isNotBlank(xLabel)) {
      chart.getXAxis().setLabel(xLabel);
    }
    if (StringUtils.isNotBlank(yLabel)) {
      chart.getYAxis().setLabel(yLabel);
    }
    for (XYChart.Series s : series) {
      chart.getData().add(s);
    }
    return chart;
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
