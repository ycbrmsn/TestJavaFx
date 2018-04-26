package application.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

public class JdbcHelper {

//  private static DriverManagerDataSource dataSource;
  private static JdbcTemplate jdbcTemplate;

//  static {
//    try {
//      // 1.注册
//      Class.forName("com.mysql.jdbc.Driver");
//      Properties conProps = new Properties();
//      InputStream is = JdbcHelper.class.getClassLoader()
//          .getResourceAsStream("application.properties");
//      conProps.load(is);
//      // 使用的是DBCP方式来加载数据库连接信息
//      dataSource = new DriverManagerDataSource(conProps.getProperty("url"), 
//          conProps.getProperty("username"), conProps.getProperty("password"));
////      dataSource.setConnectionProperties(conProps);
//    } catch (Exception e) {
//      throw new ExceptionInInitializerError(e);
//    }
//  }
  
  static {
    try {
      Properties conProps = new Properties();
      InputStream is = JdbcHelper.class.getClassLoader()
          .getResourceAsStream("application.properties");
      conProps.load(is);
      String driverClassName = conProps.getProperty("driverClassName");
      String url = conProps.getProperty("url");
      String username = conProps.getProperty("username");
      String password = conProps.getProperty("password");
      String validationQuery = conProps.getProperty("validationQuery");
      int initialSize = Integer.parseInt(conProps.getProperty("initialSize"));
      int minIdle = Integer.parseInt(conProps.getProperty("minIdle"));
      int maxActive = Integer.parseInt(conProps.getProperty("maxActive"));
      int maxWait = Integer.parseInt(conProps.getProperty("maxWait"));
      int timeBetweenEvictionRunsMillis = Integer.parseInt(conProps.getProperty("timeBetweenEvictionRunsMillis"));
      int minEvictableIdleTimeMillis = Integer.parseInt(conProps.getProperty("minEvictableIdleTimeMillis"));
      DruidDataSource dataSource = new DruidDataSource();
      dataSource.setDriverClassName(driverClassName);
      dataSource.setUrl(url);
      dataSource.setUsername(username);
      dataSource.setPassword(password);
      dataSource.setInitialSize(initialSize);
      dataSource.setMinIdle(minIdle);
      dataSource.setMaxActive(maxActive);
      dataSource.setMaxWait(maxWait);
      dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
      dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
      dataSource.setValidationQuery(validationQuery);
      jdbcTemplate = new JdbcTemplate(dataSource);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

//  // 获得数据源
//  public static DriverManagerDataSource getDataSource() {
//    return dataSource;
//  }
//
//  public static Connection getConnection() throws SQLException {
//    return dataSource.getConnection();
//
//  }
//
//  public static void free(ResultSet rs, Statement st, Connection conn) {
//    try {
//      if (rs != null)
//        rs.close();
//    } catch (SQLException e) {
//      e.printStackTrace();
//    } finally {
//      try {
//        if (st != null)
//          st.close();
//      } catch (SQLException e) {
//        e.printStackTrace();
//      } finally {
//        if (conn != null)
//          try {
//            conn.close();
//          } catch (Exception e) {
//            e.printStackTrace();
//          }
//      }
//    }
//  }
  
}
