package main.service;

import main.data.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Transactional
public interface UserService {
    List<UserEntity> findAllUsers();
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findById(Long id);
    boolean existsByUsername(String username);
    UserEntity createUser(UserEntity userEntity);
    UserEntity updateUser(UserEntity userEntity);
    UserEntity getUser(String username);
    void deleteUser(UserEntity userEntity);
}
