package main.data.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "t_users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String roles;
    @Column(name = "balance")
    private Integer balance;

    public UserEntity() {}
    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles = "User";
        this.balance = 1000;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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
    public String getRole() {
        return roles;
    }
    public void setRole(String role) {
        this.roles = roles;
    }
    public Integer getBalance() {
        return balance;
    }
    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
