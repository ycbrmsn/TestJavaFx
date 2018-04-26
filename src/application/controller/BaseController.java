package application.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class BaseController implements Initializable {
  
  protected Map<String, Object> parameter = new HashMap<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // TODO Auto-generated method stub
  }
  
  public void addParameter(String key, Object value) {
    parameter.put(key, value);
  }
  
  public void addParameters(Map<String, Object> param) {
    parameter.putAll(param);
  }
  
  public void removeParameter(String key) {
    parameter.remove(key);
  }
  
  @SuppressWarnings("unchecked")
  public <T> T getParameter(String key) {
    return (T) parameter.get(key);
  }

  public Map<String, Object> getParameter() {
    return parameter;
  }

  public void setParameter(Map<String, Object> parameter) {
    this.parameter = parameter;
  }

}
