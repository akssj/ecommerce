package alledrogo.data.repository;

import alledrogo.data.entity.UserEntity;
import alledrogo.data.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository interface
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByUsername(String username);
    List<UserEntity> findByEmail(String username);
    Optional<UserEntity> findByUsernameAndAccountStatus(String username, UserStatus userStatus);
    Optional<UserEntity> findByEmailAndAccountStatus(String email, UserStatus userStatus);
    boolean existsByUsername(String username);
    boolean existsByEmail(String userEmail);
}
