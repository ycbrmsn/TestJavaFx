package application.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import application.helper.ActionHelper;
import application.helper.FrameHelper;
import application.service.LeftMenuService;
import application.vo.TreeMenu;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class LeftMenuController extends BaseController {
  
  @FXML
  private TreeView<TreeMenu> treeView1;
  
  @FXML
  private Separator separator;
  
  @FXML
  private TabPane tabPane;
  
  private TreeItem<TreeMenu> root;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initViews();
  }
  
  /**
   * 查询菜单数据，并组装成树节点
   * @return 树根节点
   */
  private TreeItem<TreeMenu> getEnabledTreeMenu() {
    List<TreeMenu> menus = LeftMenuService.getEnabledMenus(); // 查询有效菜单集合
    Map<String, TreeItem<TreeMenu>> map = new HashMap<>();
    TreeItem<TreeMenu> root = null;
    for (TreeMenu treeMenu : menus) {
      TreeItem<TreeMenu> treeItem = new TreeItem<TreeMenu>(treeMenu);
      Integer id = treeMenu.getId();
      map.put(String.valueOf(id), treeItem);
      Integer pid = treeMenu.getPid();
      if (pid != null) { // 如果有上级，则加入到上级中
        TreeItem<TreeMenu> pTreeMenu = map.get(String.valueOf(pid));
        pTreeMenu.getChildren().add(treeItem);
      } else {
        root = treeItem;
        root.setExpanded(true);
      }
    }
    return root;
  }
  
  private void initViews() {
//    tabPane.prefHeightProperty().bind(centerPane.heightProperty());
    initMenu();
    initSeparator();
  }
  
  /**
   * 初始化菜单
   */
  private void initMenu() {
    ActionHelper.run(() -> {
      root = getEnabledTreeMenu();
    }, () -> {
      long t1 = new java.util.Date().getTime();
      treeView1.setCellFactory(
        param -> new TreeCell<TreeMenu>() {
          @Override
          protected void updateItem(TreeMenu item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
              setText(null);
              setGraphic(null);
            } else {
              setText(item.getTitle());
              setGraphic(getTreeItem().getGraphic());
              // 点击
              this.setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
//                  String target = item.getTarget();
                  String frame = item.getFrame();
                  if (StringUtils.isNotBlank(frame)) {
                    FrameHelper.addTab(String.valueOf(item.getId()), item.getTitle(), frame, "id", item.getId());
                  }
                }
              });
              // 拖动
              this.setOnDragDetected((event) -> {
                Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(this.getText() + "(id:" + this.getItem().getId() + ")");
                dragboard.setContent(content);
                event.consume();
              });
              this.setOnDragOver((event) -> {
                event.acceptTransferModes(TransferMode.MOVE);
                event.consume();
              });
              
              this.setOnDragDone((event) -> {
                Dragboard dragboard = event.getDragboard();
                FrameHelper.alert(event, dragboard.getString());
                event.consume();
              });
            }
          }
        }
      );
      treeView1.setRoot(root);
      long t2 = new java.util.Date().getTime();
      System.out.println("init menu: " + (t2 - t1));
    });
  }
  
  private void initSeparator() {
//    separator.prefHeightProperty().bind(centerPane.heightProperty());
//    separator.setOnDragDetected((event) -> {
//      Dragboard dragboard = separator.startDragAndDrop(TransferMode.MOVE);
//      ClipboardContent content = new ClipboardContent();
//      content.putString("move");
//      dragboard.setContent(content);
//      event.consume();
//    });
//    FrameHelper.getMainPane().setOnDragOver((event) -> {
//      event.acceptTransferModes(TransferMode.MOVE);
//      double x = event.getSceneX();
//      double maxWidth = mainPane.getWidth() - 10;
//      double menuWidth;
//      if (x < MENU_MIN_WIDTH) {
//        menuWidth = MENU_MIN_WIDTH;
//      } else if (x > maxWidth) {
//        menuWidth = maxWidth;
//      } else {
//        menuWidth = x;
//      }
//      tabPane.setPrefWidth(menuWidth);
//      separator.setLayoutX(menuWidth);
//      event.consume();
//    });
  }
  
 }
