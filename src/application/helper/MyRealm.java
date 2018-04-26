package application.helper;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class MyRealm extends AuthorizingRealm {
  
//  private static final Logger LOGGER = Logger.getLogger(MyRealm.class);
  
  public enum SaltStyle { NO_SALT, CRYPT, COLUMN, EXTERNAL };
  
  protected SaltStyle saltStyle = SaltStyle.NO_SALT;
  
  protected static final String DEFAULT_AUTHENTICATION_QUERY = 
    "SELECT user_.id, user_.password, user_.username, user_.realname " 
    + "FROM user user_ "  
    + "WHERE user_.username = ? ";

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    String username;
    char[] password;
    UsernamePasswordToken upToken = (UsernamePasswordToken) token;
    username = upToken.getUsername();
    password = upToken.getPassword();
    if (StringUtils.isBlank(username)) {
      throw new AccountException("用户名不能为空");
    } else if (ArrayUtils.isEmpty(password)) {
      throw new AccountException("密码不能为空");
    }
    Object[] param = new String[] {username};
    
    try {
      ShiroUser shiroUser = JdbcHelper.getJdbcTemplate().queryForObject(DEFAULT_AUTHENTICATION_QUERY, 
          param, new BeanPropertyRowMapper<ShiroUser>(ShiroUser.class));
      return new SimpleAuthenticationInfo(shiroUser, shiroUser.getPassword().toCharArray(), getName());
    } catch (EmptyResultDataAccessException e) {
      throw new AccountException("该用户不存在");
    }
  }
  
  public HashedCredentialsMatcher hashedCredentialsMatcher() {
    HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
    credentialsMatcher.setHashAlgorithmName("MD5");
    credentialsMatcher.setHashIterations(2);
    credentialsMatcher.setStoredCredentialsHexEncoded(true);
    return credentialsMatcher;
  }
  
  public static class ShiroUser implements Serializable {

    private static final long serialVersionUID = 98765458923234L;
    
    private Integer id;
    private String username;
    private String password;
    private String realname;
    
    public ShiroUser() {
      
    }
    
    public ShiroUser(Integer id, String username, String password, String realname) {
      
    }
    
    public Integer getId() {
      return id;
    }
    public void setId(Integer id) {
      this.id = id;
    }
    public String getUsername() {
      return username;
    }
    public void setUsername(String username) {
      this.username = username;
    }
    public String getPassword() {
      return password;
    }
    public void setPassword(String password) {
      this.password = password;
    }
    public String getRealname() {
      return realname;
    }
    public void setRealname(String realname) {
      this.realname = realname;
    }
    
    @Override
    public String toString() {
      return "ShiroUser [id=" + id + ", username=" + username + ", password=" + password + ", realname=" + realname
          + "]";
    }
    
  }

}
