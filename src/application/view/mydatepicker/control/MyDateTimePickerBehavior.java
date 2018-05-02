package application.view.mydatepicker.control;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.behavior.KeyBinding;

public class MyDateTimePickerBehavior extends ComboBoxBaseBehavior<LocalDateTime> {

  /***************************************************************************
   *                                                                         *
   * Constructors                                                            *
   *                                                                         *
   **************************************************************************/

  /**
   *
   */
  public MyDateTimePickerBehavior(final MyDateTimePicker datePicker) {
    super(datePicker, DATE_PICKER_BINDINGS);
  }

  /***************************************************************************
   *                                                                         *
   * Key event handling                                                      *
   *                                                                         *
   **************************************************************************/

  protected static final List<KeyBinding> DATE_PICKER_BINDINGS = new ArrayList<KeyBinding>();
  static {
    DATE_PICKER_BINDINGS.addAll(COMBO_BOX_BASE_BINDINGS);
  }

  @Override
  public void onAutoHide() {
    // when we click on some non-interactive part of the
    // calendar - we do not want to hide.
    MyDateTimePicker datePicker = (MyDateTimePicker) getControl();
    MyDateTimePickerSkin cpSkin = (MyDateTimePickerSkin) datePicker.getSkin();
    cpSkin.syncWithAutoUpdate();
    // if the MyDatePicker is no longer showing, then invoke the super method
    // to keep its show/hide state in sync.
    if (!datePicker.isShowing())
      super.onAutoHide();
  }
}
