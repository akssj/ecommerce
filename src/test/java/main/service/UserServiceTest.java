package main.service;

import main.data.entity.UserEntity;
import main.data.repository.UserRepository;
import main.service.implementation.UserServiceImpl;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeMethod
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllUsersTest() {

        List<UserEntity> mockUsers = Arrays.asList(
                new UserEntity("MockTestUser0", "MockTestPassword0"),
                new UserEntity("MockTestUser1", "MockTestPassword1")
        );
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<UserEntity> result = userServiceImpl.findAllUsers();

        assertEquals(mockUsers.size(), result.size());

        for (int i = 0; i < mockUsers.size(); i++) {
            UserEntity expectedUser = mockUsers.get(i);
            UserEntity actualUser = result.get(i);

            assertEquals(expectedUser.getUsername(), actualUser.getUsername());
            assertEquals(expectedUser.getPassword(), actualUser.getPassword());
            assertEquals(expectedUser.getRole(), actualUser.getRole());
            assertEquals(expectedUser.getBalance(), actualUser.getBalance());
        }

        verify(userRepository, times(1)).findAll();
    }
}
