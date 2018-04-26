package application.controller;

import static application.helper.Constant.DATABASE_FILE_ERROR;
import static application.helper.Constant.INNER_ERROR_MESSAGE;

import java.net.URL;
import java.util.ResourceBundle;

import application.exception.CustomException;
import application.helper.ActionHelper;
import application.helper.FrameHelper;
import application.service.LoginService;
import application.service.UserService;
import application.stage.MainStage;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController extends BaseController {
  
  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private CheckBox rememberMeCheckbox;
  @FXML
  private Button loginBtn;
  
  @FXML
  private TextField newUsernameField;
  @FXML
  private PasswordField newPasswordField;
  @FXML
  private PasswordField newRepasswordField;
  @FXML
  private TextField newRealnameField;
  @FXML
  private Button registerBtn;
  
  private String initUsername;
  private String errorMessage;
  private String loginMessage;
  private String registerMessage;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    init();
  }
  
  /**
   * 登录按钮点击方法
   * @param event
   */
  @FXML
  private void login(Event event) {
    String username = usernameField.getText();
    String password = passwordField.getText();
    boolean rememberMe = rememberMeCheckbox.isSelected();
    disableAction(event);
    ActionHelper.run(() -> {
      loginMessage = LoginService.login(username, password, rememberMe);
    }, () -> {
      ableAction();
      if (loginMessage == null) {
        FrameHelper.closeWindow(usernameField);
        MainStage.show();
      } else {
        FrameHelper.alert(event, loginMessage);
      }
    });
  }
  
  /**
   * 注册按钮点击方法
   * @param event
   */
  @FXML
  private void register(Event event) {
    String username = newUsernameField.getText();
    String password = newPasswordField.getText();
    String repassword = newRepasswordField.getText();
    String realname = newRealnameField.getText();
    disableAction(event);
    ActionHelper.run(() -> {
      try {
        UserService.addUser(username, password, repassword, realname);
        registerMessage = null;
      } catch (CustomException e) {
        registerMessage = e.getMessage();
      } catch (Exception e) {
        e.printStackTrace();
        registerMessage = INNER_ERROR_MESSAGE;
      }
    }, () -> {
      ableAction();
      if (registerMessage == null) {
        LoginService.login(username, repassword, true);
        FrameHelper.closeWindow(newUsernameField);
        MainStage.show();
      } else {
        FrameHelper.error(event, registerMessage);
      }
    });
    
  }
  
  /**
   * 禁用按钮
   * @param disabled
   */
  private void disableNode(boolean disabled) {
    Node[] nodes = new Node[] {
        usernameField, passwordField, rememberMeCheckbox, loginBtn, 
        newUsernameField, newPasswordField, newRepasswordField, newRealnameField, registerBtn
    };
    for (Node node : nodes) {
      node.setDisable(disabled);
    }
  }
  
  private void ableAction() {
    disableNode(false);
    FrameHelper.hideLoading();
  }
  
  private void disableAction(Event event) {
    disableNode(true);
    FrameHelper.showLoading(event);
  }
  
  @FXML
  private void triggerLogin(KeyEvent event) {
    KeyCode keyCode = event.getCode();
    if (keyCode == KeyCode.ENTER) {
      login(event);
    }
  }
  
  @FXML
  private void triggerRegister(KeyEvent event) {
    KeyCode keyCode = event.getCode();
    if (keyCode == KeyCode.ENTER) {
      register(event);
    }
  }

  private void init() {
    ActionHelper.run(() -> {
      try {
        initUsername = UserService.getRememberUser();
        errorMessage = null;
      } catch (Exception e) {
        e.printStackTrace();
        errorMessage = INNER_ERROR_MESSAGE;
      }
    }, () -> {
      if (errorMessage == null) {
        usernameField.setText(initUsername);
      } else {
        FrameHelper.error(usernameField, DATABASE_FILE_ERROR);
      }
    });
  }
}
