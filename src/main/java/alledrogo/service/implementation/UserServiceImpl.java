package alledrogo.service.implementation;

import alledrogo.data.entity.UserEntity;
import alledrogo.data.enums.UserStatus;
import alledrogo.data.repository.UserRepository;
import alledrogo.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * UserService Implementation class.
 * Overrides methods from UserService introducing further logic.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserEntity> findAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsernameAndAccountStatus(username, UserStatus.STATUS_ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Active user not found with username: " + username));
    }

    @Override
    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .filter(userEntity -> userEntity.getAccountStatus() == UserStatus.STATUS_ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsernameAndAccountStatus(username, UserStatus.STATUS_ACTIVE)
                .isPresent();
    }

    @Override
    public boolean existsByEmail(String userEmail) {
        return userRepository.findByEmailAndAccountStatus(userEmail, UserStatus.STATUS_ACTIVE)
                .isPresent();
    }

    @Override
    public boolean createUser(UserEntity newUserEntity) {
        Optional<UserEntity> existingUser = userRepository.findByUsernameAndAccountStatus(newUserEntity.getUsername(), UserStatus.STATUS_ACTIVE);
        if (existingUser.isPresent()) {
            return false;
        }
        return newUserEntity == userRepository.save(newUserEntity);
    }

    @Override
    public boolean updateUser(UserEntity userEntity){
        if (userRepository.existsById(userEntity.getId())){
            return userEntity == userRepository.save(userEntity);
        }else {
            return false;
        }
    }

    @Override
    public boolean deleteUser(UserEntity userEntity) {
        if (userRepository.existsById(userEntity.getId())) {
            userRepository.delete(userEntity);
            return true;
        }
        return false;
    }

}
