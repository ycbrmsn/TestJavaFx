package application.service;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import application.helper.JdbcHelper;
import application.vo.TreeMenu;

public class LeftMenuService {
  
  public static List<TreeMenu> getEnabledMenus() {
    String sql = "SELECT id, pid, title, icon, level, order_by, target, frame FROM menu WHERE enabled = 'Y' ORDER BY level, order_by";
    return JdbcHelper.getJdbcTemplate().query(sql, new BeanPropertyRowMapper<TreeMenu>(TreeMenu.class));
  }
  
  public static void createTableMenu() {
    String sql = "CREATE TABLE menu(id INT PRIMARY KEY NOT NULL, pid INT, "
        + "title VARCHAR(100), icon VARCHAR(100), level TINYINT, target VARCHAR(20), "
        + "frame VARCHAR(100), order_by TINYINT, enabled CHAR(1))";
    JdbcHelper.getJdbcTemplate().execute(sql);
  }
  
  public static void main(String[] args) {
    System.out.println(getEnabledMenus());
//    createTableMenu();
  }
}
