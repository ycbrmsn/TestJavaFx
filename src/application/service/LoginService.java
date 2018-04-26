package application.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;

import application.helper.MyRealm;

public class LoginService {
  
  static {
    MyRealm realm = new MyRealm();
    realm.setCredentialsMatcher(realm.hashedCredentialsMatcher());
    DefaultSecurityManager securityManager = new DefaultSecurityManager(realm);
    SecurityUtils.setSecurityManager(securityManager);
  }

  public static String login(String username, String password, boolean rememberMe) {
    Subject currentUser = SecurityUtils.getSubject();
    
    String message = null;
    if (!currentUser.isAuthenticated()) {
      UsernamePasswordToken token = new UsernamePasswordToken(username, password);
      try {
        currentUser.login(token);
        UserService.rememberMe(rememberMe);
      } catch (LockedAccountException e) {
        message = "用户locked";
      } catch (AccountException e) {
        message = e.getMessage();
      } catch (IncorrectCredentialsException e) {
        message = "密码错误";
      } catch (AuthenticationException e) {
        message = "认证失败";
      }
    }
    
    return message;
  }
  
  public static void logout() {
    Subject currentUser = SecurityUtils.getSubject();
    currentUser.logout();
  }
  
  public static void main(String[] args) {
    String password = "123456";
    System.out.println(UserService.encyptPassword(password));
  }
}
