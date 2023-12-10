package main.security.service;

import main.data.entity.UserEntity;
import main.service.UserService;
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
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
    try{
      UserEntity userEntity = userService.findByUsername(username);
      return new UserDetailsImpl(userEntity);
    }catch (Exception e){
      throw new UsernameNotFoundException("Username Not Found Exception: " + e);
    }
  }
}
