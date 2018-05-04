package application.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import application.helper.JdbcHelper;
import application.vo.Person;

public class PersonService {
  
  private static final String TABLE_NAME = "person";
  
  public static Logger LOGGER = Logger.getLogger(PersonService.class);
  
  public static List<Person> getPerson(int pageIndex, int pageSize, Map<String, String> parameter) {
    String name = parameter.get("name");
    String timeStart = parameter.get("timeStart");
    String timeFinish = parameter.get("timeFinish");
    int offset = pageIndex * pageSize;
    int rows = pageSize;
    String sql = "SELECT id, name, gender, age, DATE(birthday) birthday FROM " + TABLE_NAME + " WHERE 1 = 1";
    List<Object> paramList = new ArrayList<>();
    if (StringUtils.isNotBlank(name)) {
      sql += " AND name LIKE '%' || ? || '%'";
      paramList.add(name);
    }
    if (StringUtils.isNotBlank(timeStart)) {
      sql += " AND birthday >= DATE(?)";
      paramList.add(timeStart);
    }
    if (StringUtils.isNotBlank(timeFinish)) {
      sql += " AND birthday <= DATE(?)";
      paramList.add(timeFinish);
    }
    sql += " ORDER BY id LIMIT ?, ?";
    paramList.add(offset);
    paramList.add(rows);
    long l1 = new java.util.Date().getTime();
    List<Person> list = JdbcHelper.getJdbcTemplate().query(sql, paramList.toArray(), new BeanPropertyRowMapper<Person>(Person.class));
    long l2 = new java.util.Date().getTime();
    System.out.println("PersonService.getPerson: cost " + (l2 - l1) + " millisecond");
    return list;
  }
  
  public static int countPerson(Map<String, String> parameter) {
    String name = parameter.get("name");
    String timeStart = parameter.get("timeStart");
    String timeFinish = parameter.get("timeFinish");
    String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE 1 = 1";
    List<Object> paramList = new ArrayList<>();
    if (StringUtils.isNotBlank(name)) {
      sql += " AND name LIKE '%' || ? || '%'";
      paramList.add(name);
    }
    if (StringUtils.isNotBlank(timeStart)) {
      sql += " AND birthday >= DATE(?)";
      paramList.add(timeStart);
    }
    if (StringUtils.isNotBlank(timeFinish)) {
      sql += " AND birthday <= DATE(?)";
      paramList.add(timeFinish);
    }
    long l1 = new java.util.Date().getTime();
    int total = JdbcHelper.getJdbcTemplate().queryForObject(sql, paramList.toArray(), Integer.class);
    long l2 = new java.util.Date().getTime();
    System.out.println("PersonService.countPerson: cost " + (l2 - l1) + " millisecond");
    return total;
  }
  
  public static Person getPersonById(Integer id) {
    String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    Object[] param = new Object[] {id};
    Person person = null;
    try {
      person = JdbcHelper.getJdbcTemplate().queryForObject(sql, param, new BeanPropertyRowMapper<Person>(Person.class));
    } catch (EmptyResultDataAccessException e) {
      System.out.println("happened in application.service.PersonService.getPersonById()");
      System.out.println(e);
    }
    return person;
  }
  
  public static void main(String[] args) {
    Map<String, String> param = new HashMap<>();
    param.put("name", "小");
    System.out.println(getPerson(0, 3, param));
  }
}
