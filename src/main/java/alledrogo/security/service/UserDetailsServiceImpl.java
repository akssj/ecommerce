package alledrogo.security.service;

import alledrogo.data.entity.UserEntity;
import alledrogo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserService userService;

  @Autowired
  public UserDetailsServiceImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userService.findByUsername(username);

    if (userEntity == null) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    switch (userEntity.getAccountStatus()) {
      case STATUS_DELETED:
      case STATUS_BANNED:
        throw new UsernameNotFoundException("User account is disabled");
      case STATUS_ACTIVE:
        return new UserDetailsImpl(userEntity);
      default:
        throw new IllegalStateException("Unexpected value: " + userEntity.getAccountStatus());
    }
  }
}

