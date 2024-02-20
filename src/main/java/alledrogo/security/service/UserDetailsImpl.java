package alledrogo.security.service;

import alledrogo.data.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;

public class UserDetailsImpl implements UserDetails {
  @Serial
  private static final long serialVersionUID = 1L;
  @Autowired
  private final UserEntity userEntity;
  @Autowired
  public UserDetailsImpl(UserEntity userEntity) {
    this.userEntity = userEntity;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new HashSet<>();
    authorities.add(new SimpleGrantedAuthority(userEntity.getRole().name()));
    return authorities;
  }
  public Long getId(){
    return userEntity.getId();
  }

  @Override
  public String getPassword() {
    return userEntity.getPassword();
  }

  @Override
  public String getUsername() {
    return userEntity.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return switch (userEntity.getAccountStatus()) {
      case STATUS_DELETED, STATUS_BANNED -> false;
      case STATUS_ACTIVE -> true;
    };
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
