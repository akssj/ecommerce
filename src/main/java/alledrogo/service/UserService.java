package alledrogo.service;

import alledrogo.data.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserService {
    List<UserEntity> findAllUsers();
    UserEntity findByUsername(String username);
    UserEntity findById(Long id);

    boolean existsByUsername(String username);
    boolean createUser(UserEntity userEntity);
    boolean updateUser(UserEntity userEntity);
    boolean deleteUser(UserEntity userEntity);
}
