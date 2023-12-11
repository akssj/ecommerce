package main.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import main.data.entity.UserEntity;
import main.data.repository.UserRepository;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    public UserEntity findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("User not found with username: " + username));
    }
    @Override
    public UserEntity findById(Long id){
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);
        return userEntityOptional.orElse(null);
    }
    @Override
    public boolean existsByUsername(String username){
        return userRepository.findByUsername(username).isPresent();
    }
    @Override
    public boolean createUser(UserEntity userEntity){
        return userEntity == userRepository.save(userEntity);
    }
    @Override
    public boolean updateUser(UserEntity userEntity){
        return userEntity == userRepository.save(userEntity);
    }
    @Override
    public boolean deleteUser(UserEntity userEntity){
        try {
            userRepository.delete(userEntity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
