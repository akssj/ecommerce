package alledrogo.service;

import alledrogo.data.entity.UserEntity;
import alledrogo.data.repository.UserRepository;
import alledrogo.service.implementation.UserServiceImpl;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl;
    private List<UserEntity> mockUsers;
    private void setUpMockUsers(){
        mockUsers = Arrays.asList(
                new UserEntity("TempTestUser01", "TempTestUser01"),
                new UserEntity("TempTestUser02", "TempTestUser02"),
                new UserEntity("TempTestUser03", "TempTestUser03"),
                new UserEntity("TempTestUser04", "TempTestUser04")
        );
    }

    @BeforeClass
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setUpMockUsers();
    }

    @Test(priority = 1)
    void findAllUsersTest() {
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<UserEntity> result = userServiceImpl.findAllUsers();

        assertEquals(mockUsers.size(), result.size());

        for (int i = 0; i < mockUsers.size(); i++) {
            UserEntity expectedUser = mockUsers.get(i);
            UserEntity actualUser = result.get(i);

            assertEquals(expectedUser.getId(), actualUser.getId());
            assertEquals(expectedUser.getUsername(), actualUser.getUsername());
            assertEquals(expectedUser.getPassword(), actualUser.getPassword());
            assertEquals(expectedUser.getRole(), actualUser.getRole());
            assertEquals(expectedUser.getBalance(), actualUser.getBalance());
        }

        verify(userRepository, times(1)).findAll();
    }
    @Test(priority = 2)
    void findByUsernameTest() {
        when(userRepository.findByUsername("TempTestUser03")).thenReturn(Optional.of(mockUsers.get(2)));

        UserEntity expectedUser = mockUsers.get(2);
        UserEntity actualUser = userServiceImpl.findByUsername("TempTestUser03");

        assertEquals(mockUsers.get(2), actualUser);

        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        assertEquals(expectedUser.getRole(), actualUser.getRole());
        assertEquals(expectedUser.getBalance(), actualUser.getBalance());

        verify(userRepository, times(1)).findByUsername("TempTestUser03");
    }

    @Test(priority = 3)
    void findByIdTest() {
        when(userRepository.findById(3L)).thenReturn(Optional.of(mockUsers.get(3)));

        UserEntity expectedUser = mockUsers.get(3);
        UserEntity actualUser = userServiceImpl.findById(3L);

        assertEquals(mockUsers.get(3), actualUser);

        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        assertEquals(expectedUser.getRole(), actualUser.getRole());
        assertEquals(expectedUser.getBalance(), actualUser.getBalance());

        verify(userRepository, times(1)).findById(3L);
    }
    @Test(priority = 4)
    void existsByUsernameTest() {
        when(userRepository.existsByUsername("TempTestUser01")).thenReturn(true);

        boolean userExists = userServiceImpl.existsByUsername("TempTestUser01");

        assertTrue(userExists);

        verify(userRepository, times(1)).existsByUsername("TempTestUser01");
    }

    @Test(priority = 5)
    void createUserTest() {

        UserEntity userToCreate = new UserEntity("userToCreate", "userToCreate");

        when(userRepository.save(userToCreate)).thenReturn(userToCreate);

        boolean userCreated = userServiceImpl.createUser(userToCreate);

        assertTrue(userCreated);

        verify(userRepository, times(1)).save(userToCreate);
    }

    @Test(priority = 6)
    void updateUserTest() {
        UserEntity userToUpdate = new UserEntity("userToUpdate", "userToUpdate");
        userToUpdate.setId(0L);

        when(userRepository.existsById(userToUpdate.getId())).thenReturn(true);
        when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);

        boolean userUpdated = userServiceImpl.updateUser(userToUpdate);

        assertTrue(userUpdated);

        verify(userRepository, times(1)).existsById(userToUpdate.getId());
        verify(userRepository, times(1)).save(userToUpdate);
    }

    @Test(priority = 7)
    void deleteUserTest() {

        UserEntity userToDelete = new UserEntity("userToDelete", "userToDelete");
        userToDelete.setId(1L);

        when(userRepository.existsById(userToDelete.getId())).thenReturn(true);
        doNothing().when(userRepository).delete(userToDelete);

        boolean userDeleted = userServiceImpl.deleteUser(userToDelete);

        assertTrue(userDeleted);
        verify(userRepository, times(1)).existsById(userToDelete.getId());
        verify(userRepository, times(1)).delete(userToDelete);
    }

}
