package alledrogo.service;

import alledrogo.data.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * UserService interface
 */
@Transactional
public interface UserService {
    List<UserEntity> findAllUsers();
    UserEntity findByUsername(String username);
    UserEntity findById(Long id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String userEmail);
    boolean createUser(UserEntity userEntity);
    boolean updateUser(UserEntity userEntity);
    boolean deleteUser(UserEntity userEntity);
}
