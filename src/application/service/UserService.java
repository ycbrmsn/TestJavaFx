package application.service;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.SimpleByteSource;
import org.springframework.dao.DataAccessException;
import org.sqlite.SQLiteException;

import application.exception.CustomException;
import application.helper.JdbcHelper;
import application.helper.MyRealm.ShiroUser;

public class UserService {
  
  private static final String TABLE_NAME = "user";
  
  public static void addUser(String username, String password, String repassword, String realname) {
    validateNewUser(username, password, repassword);
    password = encyptPassword(password);
    int id = getMaxUserId();
    addUser(id, username, password, realname);
  }

  /**
   * 新增用户
   * @param username
   * @param password
   * @param realname
   */
  public static void addUser(Integer id, String username, String password, String realname) {
    String sql = "INSERT INTO " + TABLE_NAME + "(id, username, password, realname) VALUES(?, ?, ?, ?)";
    Object[] param = new Object[] {id, username, password, realname};
    JdbcHelper.getJdbcTemplate().update(sql, param);
  }
  
  /**
   * 查询最大的用户id再加1
   * @return
   */
  public static int getMaxUserId() {
    String sql = "SELECT MAX(id) FROM " + TABLE_NAME;
    Integer total = JdbcHelper.getJdbcTemplate().queryForObject(sql, Integer.class);
    if (total == null) {
      return 1;
    } else {
      return total + 1;
    }
  }
  
  /**
   * 记住用户名
   * @param id
   * @param rememberMe
   */
  public static void rememberMe(Integer id) {
    String sql = "UPDATE " + TABLE_NAME + " SET remember_me = 'Y' WHERE id = ?";
    Object[] param = new Object[] {id};
    JdbcHelper.getJdbcTemplate().update(sql, param);
  }
  
  /**
   * 记住用户名
   * @param rememberMe
   */
  public static void rememberMe(boolean rememberMe) {
    Subject currentUser = SecurityUtils.getSubject();
    ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipal();
    forgetMe();
    if (rememberMe) {
      rememberMe(shiroUser.getId());
    }
  }
  
  public static String getRememberUser() {
    String sql = "SELECT username FROM " + TABLE_NAME + " WHERE remember_me = 'Y'";
    try {
      return JdbcHelper.getJdbcTemplate().queryForObject(sql, String.class);
    } catch (DataAccessException e) {
      Throwable throwable = e.getCause();
      if (throwable != null && throwable instanceof SQLiteException) {
        throw new RuntimeException(throwable.getMessage());
      } else {
        return "";
      }
    }
  }
  
  /**
   * 忘记我
   */
  public static void forgetMe() {
    String sql = "UPDATE " + TABLE_NAME + " SET remember_me = NULL WHERE remember_me = 'Y'";
    JdbcHelper.getJdbcTemplate().update(sql);
  }
  
  /**
   * 加密密码
   * @param password
   * @return
   */
  public static String encyptPassword(String password) {
    Hash hash = new SimpleHash("MD5", new SimpleByteSource(password), null, 2);
    return hash.toHex();
  }
  
  /**
   * 验证新用户数据
   * @param username
   * @param password
   * @param repassword
   */
  private static void validateNewUser(String username, String password, String repassword) {
    if (StringUtils.isBlank(username)) {
      throw new CustomException("用户名不能为空");
    }
    if (StringUtils.isBlank(password)) {
      throw new CustomException("密码不能为空");
    }
    if (StringUtils.isBlank(repassword)) {
      throw new CustomException("密码确认不能为空");
    }
    if (!password.equals(repassword)) {
      throw new CustomException("密码与密码确认不一致");
    }
  }
}
