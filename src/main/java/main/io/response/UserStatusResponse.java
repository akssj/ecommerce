package main.io.response;

import java.io.Serializable;

public class UserStatusResponse implements Serializable {
    private Long id;
    private String username;
    private String roles;
    private Integer balance;
    public UserStatusResponse(Long id, String username, String roles, Integer balance){
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.balance = balance;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getRoles() {return roles;}
    public void setRoles(String roles) {this.roles = roles;}
    public Integer getBalance() {return balance;}
    public void setBalance(Integer balance) {this.balance = balance;}
}
