package alledrogo.io.response;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String username;
  private Collection<? extends GrantedAuthority> roles;
  private Integer balance;

  public JwtResponse(String token, Long id, String username, Collection<? extends GrantedAuthority> roles, Integer balance) {
    this.token = token;
    this.id = id;
    this.username = username;
    this.roles = roles;
    this.balance = balance;
  }

  public Long getId() {return id;}
  public void setId(Long id) {
    this.id = id;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public Integer getBalance() {
    return balance;
  }
  public void setBalance(Integer balance) {
    this.balance = balance;
  }
  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public Collection<? extends GrantedAuthority> getRoles() {return roles;}
  public void setRoles(Collection<? extends GrantedAuthority> roles) {this.roles = roles;}
}
