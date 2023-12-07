package main.service.implementation;

import main.data.entity.UserEntity;
import main.data.repository.UserRepository;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
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
    public Optional<UserEntity> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    @Override
    public Optional<UserEntity> findById(Long id){
        return userRepository.findById(id);
    }
    @Override
    public boolean existsByUsername(String username){
        return userRepository.findByUsername(username).isPresent();
    }
    @Override
    public UserEntity createUser(UserEntity userEntity){
        return userRepository.save(userEntity);
    }
    @Override
    public UserEntity updateUser(UserEntity userEntity){
        return userRepository.save(userEntity);
    }
    @Override
    public void deleteUser(UserEntity userEntity){
        userRepository.delete(userEntity);
    }
    @Override
    public UserEntity getUser(String username){

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        return userEntityOptional.get();
    }
}
