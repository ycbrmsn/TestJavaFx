package application.helper;

import static application.helper.Constant.ERROR_MESSAGE;
import static application.helper.Constant.INNER_ERROR_MESSAGE;
import static application.helper.Constant.PROMPT_MESSAGE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import application.controller.BaseController;
import application.stage.LoadingStage;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class FrameHelper {
  
  private static BorderPane mainPane;
  private static SplitPane centerPane;
  private static TabPane rightPane;
  private static Stage loadingStage;
  
  public static void addTab(String id, String text, Node content) {
    ObservableList<Tab> tabs = rightPane.getTabs();
    Tab tab = null;
    for (Tab t : tabs) {
      if (t.getId().equals(id)) {
        tab = t;
        break;
      }
    }
    if (tab == null) {
      tab = new Tab(text, content);
      tab.setClosable(true);
      tab.setId(id);
      rightPane.getTabs().add(tab);
    }
    SingleSelectionModel<Tab> selectionModel = rightPane.getSelectionModel();
    selectionModel.select(tab);
  }
  
  public static void addTab(String id, String text, String fxmlName) {
    Node content = loadView(fxmlName);
    addTab(id, text, content);
  }
  
  public static void addTab(String id, String text, String fxmlName, Map<String, Object> param) {
    Node content = loadView(fxmlName, param);
    addTab(id, text, content);
  }
  
  public static void addTab(String id, String text, String fxmlName, String key, Object value) {
    Node content = loadView(fxmlName, key, value);
    addTab(id, text, content);
  }

  public static BorderPane getMainPane() {
    return mainPane;
  }

  public static void setMainPane(BorderPane mainPane) {
    FrameHelper.mainPane = mainPane;
  }

  public static SplitPane getCenterPane() {
    return centerPane;
  }

  public static void setCenterPane(SplitPane centerPane) {
    FrameHelper.centerPane = centerPane;
  }

  public static TabPane getRightPane() {
    return rightPane;
  }

  public static void setRightPane(TabPane rightPane) {
    FrameHelper.rightPane = rightPane;
  }

  /**
   * 从fxml中加载视图
   * @param fxmlName
   * @return
   */
  public static Parent loadView(String fxmlName) {
    Parent root = null;
    try {
      root = FXMLLoader.load(FrameHelper.class.getResource("/layout/" + fxmlName + ".fxml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return root;
  }
  
  /**
   * 从fxml中加载视图，并附带参数
   * @param fxmlName
   * @param param 参数map
   * @return
   */
  public static Parent loadView(String fxmlName, Map<String, Object> param) {
    Parent root = null;
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(FrameHelper.class.getResource("/layout/" + fxmlName + ".fxml"));
      root = fxmlLoader.load();
      BaseController baseController = fxmlLoader.getController();
      baseController.addParameters(param);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return root;
  }
  
  /**
   * 从fxml中加载视图，并附带参数
   * @param fxmlName
   * @param key 参数键
   * @param value 参数值
   * @return
   */
  public static Parent loadView(String fxmlName, String key, Object value) {
    Parent root = null;
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(FrameHelper.class.getResource("/layout/" + fxmlName + ".fxml"));
      root = fxmlLoader.load();
      BaseController baseController = fxmlLoader.getController();
      baseController.addParameter(key, value);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return root;
  }
  
  /**
   * 获得当前的window
   * @author Jiangzw
   * @param event
   * @return
   */
  public static Window getWindow(Event event) {
    return ((Node) event.getSource()).getScene().getWindow();
  }
  
  public static Window getWindow(Node node) {
    return node.getScene().getWindow();
  }
  
  /**
   * 关闭窗口
   * @author Jiangzw
   * @param event
   */
//  public static void closeWindow(Event event) {
//    Window window = getWindow(event);
//    closeWindow(window);
//  }
  
  public static void closeWindow(Node node) {
    Window window = getWindow(node);
    closeWindow(window);
  }
  
  public static void closeWindow(Window window) {
    Event.fireEvent(window, new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
  }
  
  public static boolean alert(Window owner, String headerText, String title, String message) {
    Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
    alert.setHeaderText(headerText);
    alert.setTitle(title);
    alert.initOwner(owner);
    Optional<ButtonType> buttonType = alert.showAndWait();
    if (!buttonType.isPresent()) {
      return false;
    } else if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
      return true;
    } else {
      return false;
    }
  }
  
  public static boolean alert(Window owner, String message) {
    return alert(owner, null, PROMPT_MESSAGE, message);
  }
  
  public static boolean alert(Event event, String message) {
    return alert(getWindow(event), message);
  }
  
  public static boolean alert(Node node, String message) {
    return alert(getWindow(node), message);
  }
  
  public static boolean error(Event event, String message) {
    return alert(getWindow(event), null, ERROR_MESSAGE, message);
  }
  
  public static boolean error(Node node, String message) {
    return alert(getWindow(node), null, ERROR_MESSAGE, message);
  }
  
  public static boolean error(Event event) {
    return error(event, INNER_ERROR_MESSAGE);
  }
  
  public static boolean error(Node node) {
    return error(node, INNER_ERROR_MESSAGE);
  }
  
  public static void showLoading(Event event) {
    if (loadingStage == null) {
      loadingStage = LoadingStage.show(event);
    } else {
      loadingStage.show();
    }
  }
  
  public static void hideLoading() {
    if (loadingStage != null) {
      FrameHelper.closeWindow(loadingStage);
      loadingStage = null;
    }
  }
  
  public static void main(String[] args) {
    long t1 = new java.util.Date().getTime();
    List<Integer> list = new ArrayList<>();
    Map<String, Integer> map = new HashMap<>();
    int times = 100000;
    for (int i = 0; i < times; i++) {
      map.put(String.valueOf(i), i);
      list.add(times - 1 - i);
    }
    long t2 = new java.util.Date().getTime();
    Collections.sort(list);
    System.out.println("cost time: " + (t2 - t1));
  }
  
  public static Point getModalPoint(Window window, Rect rect) {
    double winX = window.getX();
    double winY = window.getY();
    double winWidth = window.getWidth();
    double winHeight = window.getHeight();
    double modalX = winX + winWidth / 2;
    double modalY = winY + winHeight / 2;
    if (rect != null) {
      modalX -= rect.getWidth() / 2;
      modalY -= rect.getHeight() / 2;
    }
    return new Point(modalX, modalY);
  }
  
  public static Point getModalPoint(Window window) {
    return getModalPoint(window, null);
  }
  
}
