package application.vo;

import java.lang.reflect.Field;

/**
 * 重写toString方法，打印所有属性
 * @author Jiangzw
 *
 */
public class BaseObject {

  @Override
  public String toString() {
    Field[] fields = getClass().getDeclaredFields();
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getName()).append(" [");
    int i = 0;
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        if (i++ != 0) {
          sb.append(", ");
        }
        sb.append(field.getName()).append("=").append(field.get(this));
      } catch (IllegalArgumentException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    sb.append("]");
    return sb.toString();
  }
}
