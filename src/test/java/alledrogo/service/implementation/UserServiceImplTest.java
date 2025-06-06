package alledrogo.service.implementation;

import alledrogo.data.entity.UserEntity;
import alledrogo.data.enums.UserRole;
import alledrogo.data.enums.UserStatus;
import alledrogo.data.repository.UserRepository;
import org.mockito.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("johndoe");
        userEntity.setPassword("johndoe");
        userEntity.setRole(UserRole.ROLE_USER);
        userEntity.setAccountStatus(UserStatus.STATUS_ACTIVE);
        userEntity.setEmail("john@example.com");
    }

    @Test(priority = 2)
    public void testFindAllUsers() {
        List<UserEntity> users = List.of(userEntity);
        when(userRepository.findAll()).thenReturn(users);

        List<UserEntity> result = userService.findAllUsers();

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getUsername(), "johndoe");
    }

    @Test(priority = 3)
    public void testFindByUsername() {
        when(userRepository.findByUsernameAndAccountStatus("johndoe", UserStatus.STATUS_ACTIVE))
                .thenReturn(Optional.of(userEntity));

        UserEntity result = userService.findByUsername("johndoe");

        assertNotNull(result);
        assertEquals(result.getEmail(), "john@example.com");
        assertEquals(result.getUsername(), "johndoe");
    }


    @Test(priority = 4)
    public void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        UserEntity result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(result.getUsername(), "johndoe");
    }

    @Test(priority = 5)
    public void testExistsByUsername() {
        when(userRepository.existsByUsername("johndoe")).thenReturn(true);

        boolean exists = userService.existsByUsername("johndoe");

        assertTrue(exists);
    }

    @Test(priority = 6)
    public void testExistsByEmail() {
        when(userRepository.findByEmailAndAccountStatus("john@example.com", UserStatus.STATUS_ACTIVE))
                .thenReturn(Optional.of(userEntity));

        boolean exists = userService.existsByEmail("john@example.com");

        assertTrue(exists);
    }


    @Test(priority = 1)
    public void testCreateUser() {
        when(userRepository.findByUsernameAndAccountStatus(anyString(), eq(UserStatus.STATUS_ACTIVE))).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity newUser = new UserEntity();
        newUser.setUsername("johndoe");
        newUser.setPassword("johndoe");
        newUser.setEmail("john@example.com");

        boolean result = userService.createUser(newUser);

        assertTrue(result);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }



    @Test(priority = 7)
    public void testUpdateUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity updatedUser = new UserEntity();
        updatedUser.setId(1L);
        updatedUser.setUsername("john_updated");
        updatedUser.setEmail("john.updated@example.com");

        boolean result = userService.updateUser(updatedUser);

        assertTrue(result, "User should be updated successfully");

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).save(updatedUser);
    }


    @Test(priority = 8)
    public void testDeleteUser() {
        when(userRepository.existsById(userEntity.getId())).thenReturn(true);
        doNothing().when(userRepository).delete(userEntity);

        boolean result = userService.deleteUser(userEntity);

        assertTrue(result, "User should be deleted successfully");
        verify(userRepository, times(2)).existsById(userEntity.getId());
        verify(userRepository, times(1)).delete(userEntity);
    }
}