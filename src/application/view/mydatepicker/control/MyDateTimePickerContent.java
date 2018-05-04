package application.view.mydatepicker.control;

import static com.sun.javafx.PlatformUtil.isMac;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.traversal.Direction;

import application.view.mydatepicker.helper.MyDatePickerHelper;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MyDateTimePickerContent extends VBox {
  protected MyDateTimePicker datePicker;
  private Button backMoreMonthButton;
  private Button backMonthButton;
  private Button forwardMonthButton;
  private Button forwardMoreMonthButton;
  private Button backYearButton;
  private Button backMoreYearButton;
  private Button forwardYearButton;
  private Button forwardMoreYearButton;
  private Label monthLabel;
  private Label yearLabel;
  protected GridPane gridPane;
  protected HBox hBox;
  protected ComboBox<String> hourComboBox;
  protected ComboBox<String> minuteComboBox;
  protected ComboBox<String> secondComboBox;

  private int daysPerWeek;
  private List<DateCell> dayNameCells = new ArrayList<DateCell>();
  private List<DateCell> weekNumberCells = new ArrayList<DateCell>();
  protected List<DateCell> dayCells = new ArrayList<DateCell>();
  private LocalDate[] dayCellDates;
  private DateCell lastFocusedDayCell = null;

  final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");

  final DateTimeFormatter monthFormatterSO = DateTimeFormatter.ofPattern("LLLL"); // Standalone month name

  final DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("y");

  final DateTimeFormatter yearWithEraFormatter = DateTimeFormatter.ofPattern("GGGGy"); // For Japanese. What to use for
                                                                                       // others??

  final DateTimeFormatter weekNumberFormatter = DateTimeFormatter.ofPattern("w");

  final DateTimeFormatter weekDayNameFormatter = DateTimeFormatter.ofPattern("ccc"); // Standalone day name

  final DateTimeFormatter dayCellFormatter = DateTimeFormatter.ofPattern("d");
  
  final DateTimeFormatter myLocalTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

  /**
   * 此处调用jdk中的国际化，需重写
   * @param key
   * @return
   */
  static String getString(String key) {
    return ControlResources.getString("MyDatePicker." + key);
  }

  MyDateTimePickerContent(final MyDateTimePicker datePicker) {
    this.datePicker = datePicker;

    getStyleClass().add("date-picker-popup");

    daysPerWeek = getDaysPerWeek();

    {
      LocalDateTime date = datePicker.getValue();
      displayedYearMonth.set((date != null) ? YearMonth.from(date) : YearMonth.now());
      if (datePicker.isShowTime()) { // 如果显示时间
        displayedLocalTime.set((date != null) ? date.toLocalTime() : LocalTime.now());
      } else {
        displayedLocalTime.set((date != null) ? date.toLocalTime() : LocalTime.MIN);
      }
    }

    displayedYearMonth.addListener((observable, oldValue, newValue) -> {
      updateValues();
    });
    
    displayedLocalTime.addListener((observable, oldValue, newValue) -> {
      updateTimePane();
    });

    getChildren().add(createMonthYearPane());

    gridPane = new GridPane() {
      @Override
      protected double computePrefWidth(double height) {
        final double width = super.computePrefWidth(height);

        // RT-30903: Make sure width snaps to pixel when divided by
        // number of columns. GridPane doesn't do this with percentage
        // width constraints. See GridPane.adjustColumnWidths().
        final int nCols = daysPerWeek + (datePicker.isShowWeekNumbers() ? 1 : 0);
        final double snaphgap = snapSpace(getHgap());
        final double left = snapSpace(getInsets().getLeft());
        final double right = snapSpace(getInsets().getRight());
        final double hgaps = snaphgap * (nCols - 1);
        final double contentWidth = width - left - right - hgaps;
        return ((snapSize(contentWidth / nCols)) * nCols) + left + right + hgaps;
      }

      @Override
      protected void layoutChildren() {
        // Prevent AssertionError in GridPane
        if (getWidth() > 0 && getHeight() > 0) {
          super.layoutChildren();
        }
      }
    };
    gridPane.setFocusTraversable(true);
    gridPane.getStyleClass().add("calendar-grid");
    gridPane.setVgap(-1);
    gridPane.setHgap(-1);

    // Add a focus owner listener to Scene when it becomes available.
    final WeakChangeListener<Node> weakFocusOwnerListener = new WeakChangeListener<Node>(
        (ov2, oldFocusOwner, newFocusOwner) -> {
          if (newFocusOwner == gridPane) {
            if (oldFocusOwner instanceof DateCell) {
              // Backwards traversal, skip gridPane.
              gridPane.impl_traverse(Direction.PREVIOUS);
            } else {
              // Forwards traversal, pass focus to day cell.
              if (lastFocusedDayCell != null) {
                Platform.runLater(() -> {
                  lastFocusedDayCell.requestFocus();
                });
              } else {
                clearFocus();
              }
            }
          }
        });
    gridPane.sceneProperty().addListener(new WeakChangeListener<Scene>((ov, oldScene, newScene) -> {
      if (oldScene != null) {
        oldScene.focusOwnerProperty().removeListener(weakFocusOwnerListener);
      }
      if (newScene != null) {
        newScene.focusOwnerProperty().addListener(weakFocusOwnerListener);
      }
    }));
    if (gridPane.getScene() != null) {
      gridPane.getScene().focusOwnerProperty().addListener(weakFocusOwnerListener);
    }

    // get the weekday labels starting with the weekday that is the
    // first-day-of-the-week according to the locale in the
    // displayed LocalDateTime
    for (int i = 0; i < daysPerWeek; i++) {
      DateCell cell = new DateCell();
      cell.getStyleClass().add("day-name-cell");
      dayNameCells.add(cell);
    }

    // Week number column
    for (int i = 0; i < 6; i++) {
      DateCell cell = new DateCell();
      cell.getStyleClass().add("week-number-cell");
      weekNumberCells.add(cell);
    }

    createDayCells();
    updateGrid();
    getChildren().add(gridPane);

    HBox timePane = createTimePane();
    timePane.managedProperty().bind(timePane.visibleProperty());
    timePane.setVisible(datePicker.isShowTime());
    getChildren().add(timePane);

    getChildren().add(createButtonBar());

    refresh();

    // RT-30511: This prevents key events from reaching the popup's owner.
    addEventHandler(KeyEvent.ANY, e -> {
      Node node = getScene().getFocusOwner();
      if (node instanceof DateCell) {
        lastFocusedDayCell = (DateCell) node;
      }

      if (e.getEventType() == KeyEvent.KEY_PRESSED) {
        switch (e.getCode()) {
        case HOME:
          goToDate(LocalDate.now(), true);
          e.consume();
          break;

        case PAGE_UP:
          if ((isMac() && e.isMetaDown()) || (!isMac() && e.isControlDown())) {
            if (!backYearButton.isDisabled()) {
              forward(-1, YEARS, true);
            }
          } else {
            if (!backMonthButton.isDisabled()) {
              forward(-1, MONTHS, true);
            }
          }
          e.consume();
          break;

        case PAGE_DOWN:
          if ((isMac() && e.isMetaDown()) || (!isMac() && e.isControlDown())) {
            if (!forwardYearButton.isDisabled()) {
              forward(1, YEARS, true);
            }
          } else {
            if (!forwardMonthButton.isDisabled()) {
              forward(1, MONTHS, true);
            }
          }
          e.consume();
          break;
        }

        node = getScene().getFocusOwner();
        if (node instanceof DateCell) {
          lastFocusedDayCell = (DateCell) node;
        }
      }

      // Consume all key events except those that control
      // showing the popup and traversal.
      switch (e.getCode()) {
      case F4:
      case F10:
      case UP:
      case DOWN:
      case LEFT:
      case RIGHT:
      case TAB:
        break;

      case ESCAPE:
        datePicker.hide();
        e.consume();
        break;

      default:
        e.consume();
      }
    });
  }

  private ObjectProperty<YearMonth> displayedYearMonth = new SimpleObjectProperty<YearMonth>(this,
      "displayedYearMonth");
  
  private ObjectProperty<LocalTime> displayedLocalTime = new SimpleObjectProperty<>(this, "displayedLocalTime");

  ObjectProperty<YearMonth> displayedYearMonthProperty() {
    return displayedYearMonth;
  }
  
  ObjectProperty<LocalTime> displayedLocalTimeProperty() {
    return displayedLocalTime;
  }

  protected BorderPane createMonthYearPane() {
    BorderPane monthYearPane = new BorderPane();
    monthYearPane.getStyleClass().add("month-year-pane");

    // Month spinner

    HBox monthSpinner = new HBox();
    monthSpinner.getStyleClass().add("spinner");

    backMoreMonthButton = new Button();
    backMoreMonthButton.getStyleClass().add("left-button");
    
    backMonthButton = new Button();
    backMonthButton.getStyleClass().add("left-button");

    forwardMonthButton = new Button();
    forwardMonthButton.getStyleClass().add("right-button");
    
    forwardMoreMonthButton = new Button();
    forwardMoreMonthButton.getStyleClass().add("right-button");

    StackPane leftMonthArrow = new StackPane();
    leftMonthArrow.getStyleClass().add("left-arrow");
    leftMonthArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    backMonthButton.setGraphic(leftMonthArrow);
    
    StackPane leftMoreMonthArrow = new StackPane();
    leftMoreMonthArrow.getStyleClass().add("left-double-arrow");
    leftMoreMonthArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    backMoreMonthButton.setGraphic(leftMoreMonthArrow);

    StackPane rightMonthArrow = new StackPane();
    rightMonthArrow.getStyleClass().add("right-arrow");
    rightMonthArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    forwardMonthButton.setGraphic(rightMonthArrow);
    
    StackPane rightMoreMonthArrow = new StackPane();
    rightMoreMonthArrow.getStyleClass().add("right-double-arrow");
    rightMoreMonthArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    forwardMoreMonthButton.setGraphic(rightMoreMonthArrow);

    backMoreMonthButton.setOnAction(t -> {
      forward(-3, MONTHS, false);
    });
    
    backMonthButton.setOnAction(t -> {
      forward(-1, MONTHS, false);
    });

    monthLabel = new Label();
    monthLabel.getStyleClass().add("spinner-label");

    forwardMonthButton.setOnAction(t -> {
      forward(1, MONTHS, false);
    });
    
    forwardMoreMonthButton.setOnAction(t -> {
      forward(3, MONTHS, false);
    });

    monthSpinner.getChildren().addAll(backMoreMonthButton, backMonthButton, monthLabel, forwardMonthButton, forwardMoreMonthButton);
    monthYearPane.setLeft(monthSpinner);

    // Year spinner

    HBox yearSpinner = new HBox();
    yearSpinner.getStyleClass().add("spinner");

    backMoreYearButton = new Button();
    backMoreYearButton.getStyleClass().add("left-button");

    backYearButton = new Button();
    backYearButton.getStyleClass().add("left-button");

    forwardYearButton = new Button();
    forwardYearButton.getStyleClass().add("right-button");
    
    forwardMoreYearButton = new Button();
    forwardMoreYearButton.getStyleClass().add("right-button");
    
    StackPane leftMoreYearArrow = new StackPane();
    leftMoreYearArrow.getStyleClass().add("left-double-arrow");
    leftMoreYearArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    backMoreYearButton.setGraphic(leftMoreYearArrow);

    StackPane leftYearArrow = new StackPane();
    leftYearArrow.getStyleClass().add("left-arrow");
    leftYearArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    backYearButton.setGraphic(leftYearArrow);

    StackPane rightYearArrow = new StackPane();
    rightYearArrow.getStyleClass().add("right-arrow");
    rightYearArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    forwardYearButton.setGraphic(rightYearArrow);
    
    StackPane rightMoreYearArrow = new StackPane();
    rightMoreYearArrow.getStyleClass().add("right-double-arrow");
    rightMoreYearArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    forwardMoreYearButton.setGraphic(rightMoreYearArrow);

    backMoreYearButton.setOnAction(t -> {
      forward(-10, YEARS, false);
    });
    
    backYearButton.setOnAction(t -> {
      forward(-1, YEARS, false);
    });

    yearLabel = new Label();
    yearLabel.getStyleClass().add("spinner-label");

    forwardYearButton.setOnAction(t -> {
      forward(1, YEARS, false);
    });
    
    forwardMoreYearButton.setOnAction(t -> {
      forward(10, YEARS, false);
    });

    yearSpinner.getChildren().addAll(backMoreYearButton, backYearButton, yearLabel, forwardYearButton, forwardMoreYearButton);
    yearSpinner.setFillHeight(false);
    monthYearPane.setRight(yearSpinner);

    return monthYearPane;
  }

  protected HBox createTimePane() {
    HBox hBox = new HBox();
    Label timeLabel = new Label("时间");
    ObservableList<String> hourList = FXCollections.observableArrayList();
    for (int i = 0; i < 24; i++) {
      hourList.add(StringUtils.leftPad(String.valueOf(i), 2, "0"));
    }
    ObservableList<String> minuteList = FXCollections.observableArrayList();
    for (int i = 0; i < 60; i++) {
      minuteList.add(StringUtils.leftPad(String.valueOf(i), 2, "0"));
    }
    double comboBoxWidth = 75d;
    hourComboBox = new ComboBox<>(hourList);
//    hourComboBox.setEditable(true);
    hourComboBox.setPrefWidth(comboBoxWidth);
    hourComboBox.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
          updateTime();
        });
    minuteComboBox = new ComboBox<>(minuteList);
//    minuteComboBox.setEditable(true);
    minuteComboBox.setPrefWidth(comboBoxWidth);
    minuteComboBox.getSelectionModel().selectedItemProperty()
      .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
      updateTime();
    });
    secondComboBox = new ComboBox<>(minuteList);
//    secondComboBox.setEditable(true);
    secondComboBox.setPrefWidth(comboBoxWidth);
    secondComboBox.getSelectionModel().selectedItemProperty()
      .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
      updateTime();
    });
    Label label1 = new Label(":");
    Label label2 = new Label(":");
    Insets labelInsets = new Insets(2);
    Insets ins5 = new Insets(5, 5, 0, 5);
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.setPadding(ins5);
    hBox.setMargin(timeLabel, new Insets(0, 5, 0, 0));
    hBox.setMargin(label1, labelInsets);
    hBox.setMargin(label2, labelInsets);
    hBox.getChildren().addAll(timeLabel, hourComboBox, label1, minuteComboBox, label2, secondComboBox);
    return hBox;
  }
  
  private String getDateTimeString() {
    return datePicker.isShowTime() ? (hourComboBox.getValue() + ":" + minuteComboBox.getValue() 
      + ":" + secondComboBox.getValue()) : "00:00:00";
  }
  
  private void updateTime() {
    LocalDateTime localDateTime = datePicker.getValue();
    if (localDateTime == null) {
      return;
    }
    LocalTime localTime = LocalTime.parse(getDateTimeString(), myLocalTimeFormatter);
    LocalDate localDate = localDateTime.toLocalDate();
    datePicker.setValue(LocalDateTime.of(localDate, localTime));
  }

  protected HBox createButtonBar() {
    HBox hBox = new HBox();
    hBox.setAlignment(Pos.CENTER_RIGHT);
    Insets buttonInsets = new Insets(0, 0, 0, 5);
    Button clearButton = new Button("清空");
    Button zeroButton = new Button("归零");
    Button todayButton = datePicker.isShowTime() ? new Button("现在") : new Button("今天");
    Button sureButton = new Button("关闭");
    clearButton.setOnAction(event -> {
      datePicker.setValue(null);
//      datePicker.hide();
    });
    zeroButton.managedProperty().bind(zeroButton.visibleProperty());
    zeroButton.setVisible(datePicker.isShowTime());
    zeroButton.setOnAction(event -> {
      hourComboBox.setValue("00");
      minuteComboBox.setValue("00");
      secondComboBox.setValue("00");
    });
    todayButton.setOnAction(event -> {
      if (datePicker.isShowTime()) { // 如果显示时间
        LocalDateTime nowDateTime = LocalDateTime.now();
        datePicker.setValue(nowDateTime);
        displayedLocalTime.set(nowDateTime.toLocalTime());
      } else {
        LocalDate nowDate = LocalDate.now();
        datePicker.setValue(nowDate.atTime(LocalTime.MIN));
        displayedLocalTime.set(LocalTime.MIN);
      }
//      datePicker.hide();
    });
    sureButton.setOnAction(event -> {
      if (datePicker.getValue() == null) {
//        datePicker.setValue(LocalDateTime.of(displ, time));
      }
      datePicker.hide();
    });
    hBox.getChildren().add(clearButton);
    hBox.getChildren().add(zeroButton);
    hBox.getChildren().add(todayButton);
    hBox.getChildren().add(sureButton);
    hBox.setMargin(zeroButton, buttonInsets);
    hBox.setMargin(clearButton, buttonInsets);
    hBox.setMargin(todayButton, buttonInsets);
    hBox.setMargin(sureButton, buttonInsets);
    hBox.setPadding(new Insets(5));
    return hBox;
  }

  private void refresh() {
    updateMonthLabelWidth();
    updateDayNameCells();
    updateValues();
    updateTimePane();
  }

  void updateValues() {
    // Note: Preserve this order, as MyDatePickerHijrahContent needs
    // updateDayCells before updateMonthYearPane().
    updateWeeknumberDateCells();
    updateDayCells();
    updateMonthYearPane();
  }

  void updateGrid() {
    gridPane.getColumnConstraints().clear();
    gridPane.getChildren().clear();

    int nCols = daysPerWeek + (datePicker.isShowWeekNumbers() ? 1 : 0);

    ColumnConstraints columnConstraints = new ColumnConstraints();
    columnConstraints.setPercentWidth(100); // Treated as weight
    for (int i = 0; i < nCols; i++) {
      gridPane.getColumnConstraints().add(columnConstraints);
    }

    for (int i = 0; i < daysPerWeek; i++) {
      gridPane.add(dayNameCells.get(i), i + nCols - daysPerWeek, 1); // col, row
    }

    // Week number column
    if (datePicker.isShowWeekNumbers()) {
      for (int i = 0; i < 6; i++) {
        gridPane.add(weekNumberCells.get(i), 0, i + 2); // col, row
      }
    }

    // setup: 6 rows of daysPerWeek (which is the maximum number of cells required
    // in the worst case layout)
    for (int row = 0; row < 6; row++) {
      for (int col = 0; col < daysPerWeek; col++) {
        gridPane.add(dayCells.get(row * daysPerWeek + col), col + nCols - daysPerWeek, row + 2);
      }
    }
  }

  void updateDayNameCells() {
    // first day of week, 1 = monday, 7 = sunday
    int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();

    // july 13th 2009 is a Monday, so a firstDayOfWeek=1 must come out of the 13th
    LocalDateTime date = LocalDateTime.of(2009, 7, 12 + firstDayOfWeek, 0, 0, 0);
    for (int i = 0; i < daysPerWeek; i++) {
      String name = weekDayNameFormatter.withLocale(getLocale()).format(date.plus(i, DAYS));
      name = name.replace("星期", "周");
      dayNameCells.get(i).setText(titleCaseWord(name));
    }
  }

  void updateWeeknumberDateCells() {
    if (datePicker.isShowWeekNumbers()) {
      final Locale locale = getLocale();
      final int maxWeeksPerMonth = 6; // TODO: Get this from chronology?

//      LocalDateTime firstOfMonth = displayedYearMonth.get().atDay(1);
      LocalDate firstOfMonth = displayedYearMonth.get().atDay(1);
      for (int i = 0; i < maxWeeksPerMonth; i++) {
        LocalDate date = firstOfMonth.plus(i, WEEKS);
        // Use a formatter to ensure correct localization,
        // such as when Thai numerals are required.
        String cellText = weekNumberFormatter.withLocale(locale).withDecimalStyle(DecimalStyle.of(locale)).format(date);
        weekNumberCells.get(i).setText(cellText);
      }
    }
  }

  void updateDayCells() {
    Locale locale = getLocale();
    Chronology chrono = getPrimaryChronology();
    int firstOfMonthIdx = determineFirstOfMonthDayOfWeek();
    YearMonth curMonth = displayedYearMonth.get();

    // RT-31075: The following are now set in the try-catch block.
    YearMonth prevMonth = null;
    YearMonth nextMonth = null;
    int daysInCurMonth = -1;
    int daysInPrevMonth = -1;
    int daysInNextMonth = -1;

    for (int i = 0; i < 6 * daysPerWeek; i++) {
      DateCell dayCell = dayCells.get(i);
      dayCell.getStyleClass().setAll("cell", "date-cell", "day-cell");
      dayCell.setDisable(false);
      dayCell.setStyle(null);
      dayCell.setGraphic(null);
      dayCell.setTooltip(null);

      try {
        if (daysInCurMonth == -1) {
          daysInCurMonth = curMonth.lengthOfMonth();
        }
        YearMonth month = curMonth;
        int day = i - firstOfMonthIdx + 1;
        // int index = firstOfMonthIdx + i - 1;
        if (i < firstOfMonthIdx) {
          if (prevMonth == null) {
            prevMonth = curMonth.minusMonths(1);
            daysInPrevMonth = prevMonth.lengthOfMonth();
          }
          month = prevMonth;
          day = i + daysInPrevMonth - firstOfMonthIdx + 1;
          dayCell.getStyleClass().add("previous-month");
        } else if (i >= firstOfMonthIdx + daysInCurMonth) {
          if (nextMonth == null) {
            nextMonth = curMonth.plusMonths(1);
            daysInNextMonth = nextMonth.lengthOfMonth();
          }
          month = nextMonth;
          day = i - daysInCurMonth - firstOfMonthIdx + 1;
          dayCell.getStyleClass().add("next-month");
        }
        LocalDate date = month.atDay(day);
        dayCellDates[i] = date;
        ChronoLocalDate cDate = chrono.date(date);

        dayCell.setDisable(false);

        if (isToday(date)) {
          dayCell.getStyleClass().add("today");
        }

        if (date.equals(datePicker.getValue())) {
          dayCell.getStyleClass().add("selected");
        }

        String cellText = dayCellFormatter.withLocale(locale).withChronology(chrono)
            .withDecimalStyle(DecimalStyle.of(locale)).format(cDate);
        dayCell.setText(cellText);

        dayCell.updateItem(date, false);
      } catch (DateTimeException ex) {
        // Date is out of range.
        // System.err.println(dayCellDate(dayCell) + " " + ex);
        dayCell.setText(" ");
        dayCell.setDisable(true);
      }
    }
  }

  private int getDaysPerWeek() {
    ValueRange range = getPrimaryChronology().range(DAY_OF_WEEK);
    return (int) (range.getMaximum() - range.getMinimum() + 1);
  }

  private int getMonthsPerYear() {
    ValueRange range = getPrimaryChronology().range(MONTH_OF_YEAR);
    return (int) (range.getMaximum() - range.getMinimum() + 1);
  }

  private void updateMonthLabelWidth() {
    if (monthLabel != null) {
      int monthsPerYear = getMonthsPerYear();
      double width = 0;
      for (int i = 0; i < monthsPerYear; i++) {
        YearMonth yearMonth = displayedYearMonth.get().withMonth(i + 1);
        String name = monthFormatterSO.withLocale(getLocale()).format(yearMonth);
        if (Character.isDigit(name.charAt(0))) {
          // Fallback. The standalone format returned a number, so use standard format
          // instead.
          name = monthFormatter.withLocale(getLocale()).format(yearMonth);
        }
        width = Math.max(width, MyDatePickerHelper.computeTextWidth(monthLabel.getFont(), name, 0));
      }
      monthLabel.setMinWidth(width);
    }
  }

  protected void updateMonthYearPane() {
    YearMonth yearMonth = displayedYearMonth.get();
    String str = formatMonth(yearMonth);
    monthLabel.setText(str);

    str = formatYear(yearMonth);
    yearLabel.setText(str);
    double width = MyDatePickerHelper.computeTextWidth(yearLabel.getFont(), str, 0);
    if (width > yearLabel.getMinWidth()) {
      yearLabel.setMinWidth(width);
    }

    Chronology chrono = datePicker.getChronology();
    LocalDate firstDayOfMonth = yearMonth.atDay(1);
    backMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, DAYS));
    forwardMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, +1, MONTHS));
    backYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, YEARS));
    forwardYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, +1, YEARS));
  }

  private String formatMonth(YearMonth yearMonth) {
    Locale locale = getLocale();
    Chronology chrono = getPrimaryChronology();
    try {
      ChronoLocalDate cDate = chrono.date(yearMonth.atDay(1));

      String str = monthFormatterSO.withLocale(getLocale()).withChronology(chrono).format(cDate);
      if (Character.isDigit(str.charAt(0))) {
        // Fallback. The standalone format returned a number, so use standard format
        // instead.
        str = monthFormatter.withLocale(getLocale()).withChronology(chrono).format(cDate);
      }
      return titleCaseWord(str);
    } catch (DateTimeException ex) {
      // Date is out of range.
      return "";
    }
  }

  private String formatYear(YearMonth yearMonth) {
    Locale locale = getLocale();
    Chronology chrono = getPrimaryChronology();
    try {
      DateTimeFormatter formatter = yearFormatter;
      ChronoLocalDate cDate = chrono.date(yearMonth.atDay(1));
      int era = cDate.getEra().getValue();
      int nEras = chrono.eras().size();

      /*
       * if (cDate.get(YEAR) < 0) { formatter = yearForNegYearFormatter; } else
       */
      if ((nEras == 2 && era == 0) || nEras > 2) {
        formatter = yearWithEraFormatter;
      }

      // Fixme: Format Japanese era names with Japanese text.
      String str = formatter.withLocale(getLocale()).withChronology(chrono)
          .withDecimalStyle(DecimalStyle.of(getLocale())).format(cDate);

      return str;
    } catch (DateTimeException ex) {
      // Date is out of range.
      return "";
    }
  }

  // Ensures that month and day names are titlecased (capitalized).
  private String titleCaseWord(String str) {
    if (str.length() > 0) {
      int firstChar = str.codePointAt(0);
      if (!Character.isTitleCase(firstChar)) {
        str = new String(new int[] { Character.toTitleCase(firstChar) }, 0, 1)
            + str.substring(Character.offsetByCodePoints(str, 0, 1));
      }
    }
    return str;
  }
  
  void updateTimePane() {
    LocalTime localTime = displayedLocalTime.getValue();
    String hour = MyDatePickerHelper.formatHMS(localTime.getHour());
    String minute = MyDatePickerHelper.formatHMS(localTime.getMinute());
    String second = MyDatePickerHelper.formatHMS(localTime.getSecond());
    if (!hour.equals(hourComboBox.getValue())) {
      hourComboBox.setValue(hour);
    }
    if (!minute.equals(minuteComboBox.getValue())) {
      minuteComboBox.setValue(minute);
    }
    if (!second.equals(secondComboBox.getValue())) {
      secondComboBox.setValue(second);
    }
  }

  /**
   * determine on which day of week idx the first of the months is
   */
  private int determineFirstOfMonthDayOfWeek() {
    // determine with which cell to start
    int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();
    int firstOfMonthIdx = displayedYearMonth.get().atDay(1).getDayOfWeek().getValue() - firstDayOfWeek;
    if (firstOfMonthIdx < 0) {
      firstOfMonthIdx += daysPerWeek;
    }
    return firstOfMonthIdx;
  }

  private boolean isToday(LocalDate localDate) {
    return (localDate.equals(LocalDate.now()));
  }

  protected LocalDate dayCellDate(DateCell dateCell) {
    assert (dayCellDates != null);
    return dayCellDates[dayCells.indexOf(dateCell)];
  }

  // public for behavior class
  public void goToDayCell(DateCell dateCell, int offset, ChronoUnit unit, boolean focusDayCell) {
    goToDate(dayCellDate(dateCell).plus(offset, unit), focusDayCell);
  }

  protected void forward(int offset, ChronoUnit unit, boolean focusDayCell) {
    YearMonth yearMonth = displayedYearMonth.get();
    DateCell dateCell = lastFocusedDayCell;
    if (dateCell == null || !dayCellDate(dateCell).getMonth().equals(yearMonth.getMonth())) {
      dateCell = findDayCellForDate(yearMonth.atDay(1));
    }
    goToDayCell(dateCell, offset, unit, focusDayCell);
  }

  // public for behavior class
  public void goToDate(LocalDate date, boolean focusDayCell) {
    if (isValidDate(datePicker.getChronology(), date)) {
      displayedYearMonth.set(YearMonth.from(date));
      if (focusDayCell) {
        findDayCellForDate(date).requestFocus();
      }
    }
  }

  // public for behavior class
  public void selectDayCell(DateCell dateCell) {
    LocalDate localDate = dayCellDate(dateCell);
    String localTimeString = getDateTimeString();
    LocalTime localTime = LocalTime.parse(localTimeString, myLocalTimeFormatter);
    datePicker.setValue(LocalDateTime.of(localDate, localTime));
    goToDate(localDate, true);
//    datePicker.hide();
  }

  private DateCell findDayCellForDate(LocalDate date) {
    for (int i = 0; i < dayCellDates.length; i++) {
      if (date.equals(dayCellDates[i])) {
        return dayCells.get(i);
      }
    }
    return dayCells.get(dayCells.size() / 2 + 1);
  }

  void clearFocus() {
    LocalDateTime focusDateTime = datePicker.getValue();
    LocalDate focusDate;
    if (focusDateTime == null) {
      focusDate = LocalDate.now();
    } else {
      focusDate = focusDateTime.toLocalDate();
    }
    if (YearMonth.from(focusDate).equals(displayedYearMonth.get())) {
      // focus date
      goToDate(focusDate, true);
    } else {
      // focus month spinner (should not happen)
      backMonthButton.requestFocus();
    }

    // RT-31857
    if (backMonthButton.getWidth() == 0) {
      backMonthButton.requestLayout();
      forwardMonthButton.requestLayout();
      backYearButton.requestLayout();
      forwardYearButton.requestLayout();
    }
  }

  protected void createDayCells() {
    final EventHandler<MouseEvent> dayCellActionHandler = ev -> {
      if (ev.getButton() != MouseButton.PRIMARY) {
        return;
      }

      DateCell dayCell = (DateCell) ev.getSource();
      selectDayCell(dayCell);
      lastFocusedDayCell = dayCell;
    };

    for (int row = 0; row < 6; row++) {
      for (int col = 0; col < daysPerWeek; col++) {
        DateCell dayCell = createDayCell();
        dayCell.addEventHandler(MouseEvent.MOUSE_CLICKED, dayCellActionHandler);
        dayCells.add(dayCell);
      }
    }

    dayCellDates = new LocalDate[6 * daysPerWeek];
  }

  private DateCell createDayCell() {
    DateCell cell = null;
    if (datePicker.getDayCellFactory() != null) {
      cell = datePicker.getDayCellFactory().call(datePicker);
    }
    if (cell == null) {
      cell = new DateCell();
    }

    return cell;
  }

  protected Locale getLocale() {
    return Locale.getDefault(Locale.Category.FORMAT);
  }

  /**
   * The primary chronology for display. This may be overridden to be different
   * than the MyDatePicker chronology. For example MyDatePickerHijrahContent uses
   * ISO as primary and Hijrah as a secondary chronology.
   */
  protected Chronology getPrimaryChronology() {
    return datePicker.getChronology();
  }

  protected boolean isValidDate(Chronology chrono, LocalDate date, int offset, ChronoUnit unit) {
    if (date != null) {
      try {
        return isValidDate(chrono, date.plus(offset, unit));
      } catch (DateTimeException ex) {
      }
    }
    return false;
  }

  protected boolean isValidDate(Chronology chrono, LocalDate date) {
    try {
      if (date != null) {
        chrono.date(date);
      }
      return true;
    } catch (DateTimeException ex) {
      return false;
    }
  }

}
