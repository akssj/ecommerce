package alledrogo.security.service;

import alledrogo.data.entity.UserEntity;
import alledrogo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
  private final UserService userService;

  @Autowired
  public UserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userService.findByUsername(username);

    if (userEntity == null) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    return new UserDetailsImpl(userEntity);
  }
}

