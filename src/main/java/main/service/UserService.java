package main.service;

import main.data.entity.ProductEntity;
import main.data.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserEntity> findAllUsers();
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findById(Long id);
    boolean existsByUsername(String username);
    UserEntity createUser(UserEntity userEntity);
    UserEntity updateUser(UserEntity userEntity);
    void deleteUser(UserEntity userEntity);
}
