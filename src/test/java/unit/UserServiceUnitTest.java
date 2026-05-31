package unit;

import com.hibernatehw.DAO.HibernateDAO;
import com.hibernatehw.model.User;
import com.hibernatehw.service.UserService;
import fixtures.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceUnitTest {

    @Mock
    private HibernateDAO hibernateDAO;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = TestDataFactory.createValidUser();
        testUser.setId(1L);
    }

    @Test
    void addUser_WithValidData_ShouldReturnTrue() {
        doAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return null;
        }).when(hibernateDAO).save(any(User.class));

        assertTrue(userService.addUser(TestDataFactory.getValidUserInput()));
        verify(hibernateDAO, times(1)).save(any(User.class));
    }

    @Test
    void addUser_WithInvalidData_ShouldReturnFalse() {
        assertFalse(userService.addUser(TestDataFactory.getInvalidUserInput()));
        verify(hibernateDAO, never()).save(any(User.class));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnOptionalWithUser() {
        Long userId = 1L;
        when(hibernateDAO.findUserById(userId)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get().getId());
        assertEquals(testUser.getName(), result.get().getName());
        assertEquals(testUser.getEmail(), result.get().getEmail());
        assertEquals(testUser.getAge(), result.get().getAge());
        verify(hibernateDAO, times(1)).findUserById(userId);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldReturnEmptyOptional() {
        Long userId = 999L;
        when(hibernateDAO.findUserById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(userId);

        assertFalse(result.isPresent());
        verify(hibernateDAO, times(1)).findUserById(userId);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDelete() {
        Long userId = 1L;
        when(hibernateDAO.findUserById(userId)).thenReturn(Optional.of(testUser));

        userService.deleteUser(userId);

        verify(hibernateDAO, times(1)).delete(userId);
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldNotDelete() {
        Long userId = 999L;
        when(hibernateDAO.findUserById(userId)).thenReturn(Optional.empty());

        userService.deleteUser(userId);

        verify(hibernateDAO, never()).delete(anyLong());
    }

    @Test
    void updateUser_WithValidData_ShouldReturnTrue() {
        String updatedData = "Обновленный updated@example.com 35";

        assertTrue(userService.updateUser(testUser, updatedData));
        verify(hibernateDAO, times(1)).update(any(User.class));
    }

    @Test
    void updateUser_WithInvalidData_ShouldReturnFalse() {
        boolean result = userService.updateUser(testUser, TestDataFactory.getInvalidUserInput());

        assertFalse(result);
        verify(hibernateDAO, never()).update(any(User.class));
    }

    @Test
    void showUsers_ShouldDisplayAllUsers() {
        List<User> users = Arrays.asList(testUser, TestDataFactory.createUserWithName("Сергей"));
        when(hibernateDAO.findAll()).thenReturn(users);

        userService.showUsers();

        verify(hibernateDAO, times(1)).findAll();
    }
}
