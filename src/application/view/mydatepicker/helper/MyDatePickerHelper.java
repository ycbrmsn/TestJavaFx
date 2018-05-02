package application.view.mydatepicker.helper;

import org.apache.commons.lang.StringUtils;

import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.tk.Toolkit;

import javafx.scene.text.Font;

@SuppressWarnings("restriction")
public class MyDatePickerHelper {

  static final TextLayout layout = Toolkit.getToolkit().getTextLayoutFactory().createLayout();

  @SuppressWarnings("deprecation")
  public static double computeTextWidth(Font font, String text, double wrappingWidth) {
    layout.setContent(text != null ? text : "", font.impl_getNativeFont());
    layout.setWrapWidth((float) wrappingWidth);
    return layout.getBounds().getWidth();
  }
  
  public static String formatHMS(int hms) {
    String str = String.valueOf(hms);
    return StringUtils.leftPad(str, 2, "0");
  }
}
