package main.service;

import main.data.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Transactional
public interface UserService {
    List<UserEntity> findAllUsers();
    UserEntity findByUsername(String username);
    UserEntity findById(Long id);
    boolean existsByUsername(String username);

    void createUser(UserEntity userEntity);
    void updateUser(UserEntity userEntity);
    void deleteUser(UserEntity userEntity);
}
