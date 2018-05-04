package application.controller;

import static application.helper.Constant.CHECK;
import static application.helper.Constant.DELETE;
import static application.helper.Constant.EDIT;
import static application.helper.Constant.PAGE_SIZE;
import static application.helper.Constant.PERSON_INFO;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import application.helper.ActionHelper;
import application.helper.DateTimeHelper;
import application.helper.FrameHelper;
import application.helper.Page;
import application.service.PersonService;
import application.view.mydatepicker.control.MyDateTimePicker;
import application.vo.Person;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class PersonListController extends BaseController {
  
  @FXML
  private TextField nameTextField;
  
  @FXML
  private Button queryBtn;
  
  @FXML
  private VBox paginationBox;
  
  @FXML
  private MyDateTimePicker timeStartDatePicker;
  
  @FXML
  private MyDateTimePicker timeFinishDatePicker;
  
  private TableView<Person> mTableView;
  private Pagination mPagination;
  private final ObservableList<Person> data = FXCollections.observableArrayList();
  
  private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  
  private Map<String, String> parameter = new HashMap<>();
  private String name;
  private LocalDateTime timeStart;
  private LocalDateTime timeFinish;
  private Page page;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
//    pagination1.getParent()
    initViews();
  }
  
  private void initViews() {
    timeStartDatePicker.setConverter(DateTimeHelper.instanceDateTimeConverTer("yyyy-MM-dd"));
    timeStartDatePicker.setShowTime(false);
    timeStartDatePicker.setValue(LocalDateTime.of(1990, 1, 4, 0, 0));
    timeFinishDatePicker.setConverter(DateTimeHelper.instanceDateTimeConverTer("yyyy-MM-dd HH"));
    queryBtn.setOnAction(new EventHandler<ActionEvent>() {
      
      @Override
      public void handle(ActionEvent event) {
        search();
      }
    });
    search();
  }
  
  @SuppressWarnings("unchecked")
  private void initTable(TableView<Person> tableView) {
    ObservableList<TableColumn<Person, ?>> columns = tableView.getColumns();
    // 第一列赋序号
    TableColumn<Person, Object> firstColumn = (TableColumn<Person, Object>) columns.get(0);
    firstColumn.setCellFactory(new Callback<TableColumn<Person, Object>, TableCell<Person, Object>>() {
      
      @Override
      public TableCell<Person, Object> call(TableColumn<Person, Object> param) {
        TableCell<Person, Object> cell = new TableCell<Person, Object>() {
          @Override
          protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              this.setText(null);
              this.setGraphic(null);
            } else {
              int rowIndex = page.getPageIndex() * PAGE_SIZE + this.getIndex() + 1;
              this.setText(String.valueOf(rowIndex));
              this.setStyle("-fx-alignment: CENTER-RIGHT;");
            }
          }
        };
        return cell;
      }

    });
    
    // 中间列
    String[] key = {"name", "gender", "age", "birthday"};
    for (int i = 1; i < columns.size() - 1; i++) {
      TableColumn<Person, Object> column = (TableColumn<Person, Object>) columns.get(i);
      column.setCellValueFactory(new PropertyValueFactory<Person, Object>(key[i - 1]));
      column.setStyle("-fx-alignment: CENTER-RIGHT;");
    }
    
    // 操作列
    TableColumn<Person, Object> lastColumn = (TableColumn<Person, Object>) columns.get(columns.size() - 1);
    lastColumn.setCellFactory(new Callback<TableColumn<Person, Object>, TableCell<Person, Object>>() {
      
      @Override
      public TableCell<Person, Object> call(TableColumn<Person, Object> param) {
//        TableCell<Person, Object> cell = new TableCell<Person, Object>() {
//          @Override
//          protected void updateItem(Object item, boolean empty) {
//            super.updateItem(item, empty);
//            if (empty) {
//              this.setText(null);
//              this.setGraphic(null);
//            } else {
//              Hyperlink link = new  Hyperlink(DETAIL);
//              link.setOnAction((event) -> {
//                FrameHelper.addTab("personInfo", PERSON_INFO, "PersonInfo");
//              });
//              this.setGraphic(link);
//            }
//          }
//        };
//        return cell;
        return new OperatePersonCell(tableView, "curd");
      }

    });
//    lastColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
  }
  
  /**
   * 搜索
   */
  private void search() {
    name = nameTextField.getText().trim();
    timeStart = timeStartDatePicker.getValue();
    timeFinish = timeFinishDatePicker.getValue();
    parameter.put("name", name);
    parameter.put("timeStart", timeStart == null ? null : timeStart.format(dateFormat));
    parameter.put("timeFinish", timeFinish == null ? null : timeFinish.format(dateTimeFormat));
    ActionHelper.run(() -> {
      int total = PersonService.countPerson(parameter);
      page = new Page(0, PAGE_SIZE, total);
    }, () -> {
      mPagination = createPagination(page.getPages(), page.getPageIndex());
      paginationBox.getChildren().clear();
      paginationBox.getChildren().add(mPagination);
    });
  }
  
  /**
   * 加载表格数据
   * @param tableView
   * @param pageIndex
   */
  private void loadData(TableView<Person> tableView) {
    nameTextField.setText(name);
    timeStartDatePicker.setValue(timeStart);
    timeFinishDatePicker.setValue(timeFinish);
    new Thread() {
      public void run() {
        List<Person> list = PersonService.getPerson(page.getPageIndex(), PAGE_SIZE, parameter);
        Platform.runLater(new Runnable() {
          
          @Override
          public void run() {
            data.clear();
            data.addAll(list);
            tableView.setItems(data);
          }
        });
      };
    }.start();
  }
  
  private Pagination createPagination(int pageCount, int pageIndex) {
    Pagination pagination = new Pagination(pageCount, pageIndex);
    pagination.setPageFactory(new Callback<Integer, Node>() {
      
      @Override
      public Node call(Integer param) {
        mTableView = createTableView();
        page.setPageIndex(param);
        loadData(mTableView);
        return mTableView;  
      }
    });
    return pagination;
  }
  
  @SuppressWarnings("unchecked")
  private TableView<Person> createTableView() {
    TableView<Person> tableView = null;
    tableView = (TableView<Person>) FrameHelper.loadView("PersonListTable");
    if (tableView != null) {
      initTable(tableView);
    }
    return tableView;
  }
  
  private class OperatePersonCell extends TableCell<Person, Object> {
    
    private Hyperlink editLink = new Hyperlink(EDIT);
    private Hyperlink checkLink = new Hyperlink(CHECK);
    private Hyperlink delLink = new Hyperlink(DELETE);
    private HBox hbox = new HBox();
    {
      editLink.getStyleClass().add("hyperlink-tablebtn");
      checkLink.getStyleClass().add("hyperlink-tablebtn");
      delLink.getStyleClass().add("hyperlink-tablebtn");
      hbox.setStyle("-fx-alignment: CENTER-RIGHT;");
    }
    
    public OperatePersonCell(TableView<Person> tableView, String permission) {
      if (permission.indexOf("u") != 0xFFFFFFFF) {
        hbox.getChildren().add(editLink);
        editLink.setOnAction((event) -> {
          FrameHelper.alert(this, "修改功能尚未实现");
        });
      }
      if (permission.indexOf("r") != 0xFFFFFFFF) {
        hbox.getChildren().add(checkLink);
        checkLink.setOnAction((event) -> {
          
          TableViewSelectionModel<Person> tableViewSelectionModel = tableView.getSelectionModel();
          int rowIndex = getTableRow().getIndex();
          tableViewSelectionModel.select(rowIndex);
          Person person = tableViewSelectionModel.getSelectedItem();
          Integer id = person.getId();
          FrameHelper.addTab("personInfo" + id, PERSON_INFO, "PersonInfo", "id", id);
        });
      }
      if (permission.indexOf("d") != 0xFFFFFFFF) {
        hbox.getChildren().add(delLink);
        delLink.setOnAction((event) -> {
          FrameHelper.alert(this, "删除功能尚未实现");
        });
      }
    }
    
    @Override
    protected void updateItem(Object item, boolean empty) {
      super.updateItem(item, empty);
      if (empty) {
        this.setText(null);
        this.setGraphic(null);
      } else {
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setGraphic(hbox);
      }
    }
    
  }
}
