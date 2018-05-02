package application.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import application.helper.JdbcHelper;
import application.vo.Person;

public class PersonService {
  
  private static final String TABLE_NAME = "person";
  
  public static Logger LOGGER = Logger.getLogger(PersonService.class);
  
  public static List<Person> getPerson(int pageIndex, int pageSize, String name) {
    int offset = pageIndex * pageSize;
    int rows = pageSize;
    String sql = "SELECT id, name, gender, age FROM " + TABLE_NAME;
    Object[] param = {offset, rows};
    if (StringUtils.isNotBlank(name)) {
      sql += " WHERE name LIKE '%' || ? || '%'";
      param = new Object[] {name, offset, rows};
    }
    sql += " ORDER BY id LIMIT ?, ?";
    long l1 = new java.util.Date().getTime();
    List<Person> list = JdbcHelper.getJdbcTemplate().query(sql, param, new BeanPropertyRowMapper<Person>(Person.class));
    long l2 = new java.util.Date().getTime();
    System.out.println("PersonService.getPerson: cost " + (l2 - l1) + " millisecond");
    return list;
  }
  
  public static int countPerson(String name) {
    String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
    Object[] param = {};
    if (StringUtils.isNotBlank(name)) {
      sql += " WHERE name LIKE '%' || ? || '%'";
      param = new Object[] {name};
    }
    long l1 = new java.util.Date().getTime();
    int total = JdbcHelper.getJdbcTemplate().queryForObject(sql, param, Integer.class);
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
    System.out.println(getPerson(0, 3, "小"));
  }
}
