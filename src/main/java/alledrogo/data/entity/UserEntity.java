package alledrogo.data.entity;

import alledrogo.data.enums.UserRole;
import alledrogo.data.enums.UserStatus;
import jakarta.persistence.*;

/**
 * UserEntity class
 */
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus accountStatus;
    @Column(name = "email")
    private String email;

    public UserEntity() {}
    public UserEntity(String username, String password, UserRole role, UserStatus accountStatus, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.accountStatus = accountStatus;
        this.email = email;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public UserRole getRole() {return role;}
    public void setRole(UserRole role) {this.role = role;}
    public UserStatus getAccountStatus() {return accountStatus;}
    public void setAccountStatus(UserStatus accountStatus) {this.accountStatus = accountStatus;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

}
