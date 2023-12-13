package alledrogo.tests.service;

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

/**
 * Tests (Unit) for userServiceImpl.
 * Creates @Mock and @InjectMocks to verify data handling by userServiceImpl and userRepository.
 * Goal is to verify that each of these methods are working as intended (verification scenarios).
 */
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

    /**
     * Verify userServiceImpl.findAllUsers() with mockUsers.
     * Check if required methods were called only once.
     * expected result: Receive list of mockUsers.
     */
    @Test(priority = 1)
    protected void findAllUsersTest() {
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

    /**
     * Verify userServiceImpl.findByUsername(username) with mockUser.
     * Check if required methods were called only once.
     * expected result: Receive correct mockUser.
     */
    @Test(priority = 2)
    protected void findByUsernameTest() {
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

    /**
     * Verify userServiceImpl.findById(idL) with mockUser.
     * Check if required methods were called only once.
     * expected result: Receive correct mockUser.
     */
    @Test(priority = 3)
    protected void findByIdTest() {
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

    /**
     * Verify userServiceImpl.existsByUsername(username) with mockUser.
     * Check if required methods were called only once.
     * expected result: Receive boolean true after check.
     */
    @Test(priority = 4)
    protected void existsByUsernameTest() {
        when(userRepository.existsByUsername("TempTestUser01")).thenReturn(true);

        boolean userExists = userServiceImpl.existsByUsername("TempTestUser01");

        assertTrue(userExists);

        verify(userRepository, times(1)).existsByUsername("TempTestUser01");
    }

    /**
     * Verify userServiceImpl.createUser(userEntity) with new UserEntity.
     * Check if required methods were called only once.
     * expected result: Receive boolean true after user creation.
     */
    @Test(priority = 5)
    protected void createUserTest() {

        UserEntity userToCreate = new UserEntity("userToCreate", "userToCreate");

        when(userRepository.save(userToCreate)).thenReturn(userToCreate);

        boolean userCreated = userServiceImpl.createUser(userToCreate);

        assertTrue(userCreated);

        verify(userRepository, times(1)).save(userToCreate);
    }

    /**
     * Verify userServiceImpl.updateUser(userEntity) with new UserEntity.
     * Check if required methods were called only once.
     * expected result: Receive boolean true after user update.
     */
    @Test(priority = 6)
    protected void updateUserTest() {
        UserEntity userToUpdate = new UserEntity("userToUpdate", "userToUpdate");
        userToUpdate.setId(0L);

        when(userRepository.existsById(userToUpdate.getId())).thenReturn(true);
        when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);

        boolean userUpdated = userServiceImpl.updateUser(userToUpdate);

        assertTrue(userUpdated);

        verify(userRepository, times(1)).existsById(userToUpdate.getId());
        verify(userRepository, times(1)).save(userToUpdate);
    }

    /**
     * Verify userServiceImpl.delete(userEntity) with new UserEntity.
     * Check if required methods were called only once.
     * expected result: Receive boolean true after user update.
     */
    @Test(priority = 7)
    protected void deleteUserTest() {

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
