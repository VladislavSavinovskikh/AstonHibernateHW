package integration;

import com.hibernatehw.DAO.HibernateDAO;
import com.hibernatehw.model.User;
import com.hibernatehw.util.TransactionHelper;
import fixtures.TestDataFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HibernateDAOIntegrationTest extends TestContainersConfig {

    private static HibernateDAO hibernateDAO;
    private static TransactionHelper transactionHelper;

    @BeforeAll
    static void setUp() {
        transactionHelper = new TransactionHelper(TestContainersConfig.sessionFactory);
        hibernateDAO = new HibernateDAO(TestContainersConfig.sessionFactory, transactionHelper);
    }

    @AfterEach
    void cleanUp() {
        transactionHelper.executeInTransaction(session -> {
            session.createMutationQuery("DELETE FROM User").executeUpdate();
        });
    }

    @Test
    @Order(1)
    void save_ShouldPersistUser() {
        User user = TestDataFactory.createValidUser();

        hibernateDAO.save(user);

        assertNotNull(user.getId());

        Optional<User> found = hibernateDAO.findUserById(user.getId());
        assertTrue(found.isPresent());
        assertEquals(user.getName(), found.get().getName());
        assertEquals(user.getEmail(), found.get().getEmail());
        assertEquals(user.getAge(), found.get().getAge());
    }

    @Test
    @Order(2)
    void findUserById_WhenUserExists_ShouldReturnUser() {
        User user = TestDataFactory.createValidUser();
        hibernateDAO.save(user);

        Optional<User> found = hibernateDAO.findUserById(user.getId());

        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getId());
        assertEquals(user.getName(), found.get().getName());
    }

    @Test
    @Order(3)
    void findUserById_WhenUserNotExists_ShouldReturnEmpty() {
        Optional<User> found = hibernateDAO.findUserById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    @Order(4)
    void findAll_ShouldReturnAllUsers() {
        User user1 = TestDataFactory.createUserWithName("Oleg");
        User user2 = TestDataFactory.createUserWithName("Alexei");
        hibernateDAO.save(user1);
        hibernateDAO.save(user2);

        List<User> users = hibernateDAO.findAll();

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("Oleg")));
        assertTrue(users.stream().anyMatch(u -> u.getName().equals("Alexei")));
    }

    @Test
    @Order(5)
    void findAll_WhenNoUsers_ShouldReturnEmptyList() {
        List<User> users = hibernateDAO.findAll();

        assertTrue(users.isEmpty());
    }

    @Test
    @Order(6)
    void update_ShouldModifyExistingUser() {
        User user = TestDataFactory.createValidUser();
        hibernateDAO.save(user);

        user.setName("ОбновленноеИмя");
        user.setEmail("updated@test.ru");
        user.setAge(45);
        hibernateDAO.update(user);

        Optional<User> updated = hibernateDAO.findUserById(user.getId());
        assertTrue(updated.isPresent());
        assertEquals("ОбновленноеИмя", updated.get().getName());
        assertEquals("updated@test.ru", updated.get().getEmail());
        assertEquals(45, updated.get().getAge());
    }

    @Test
    @Order(7)
    void delete_ShouldRemoveUser() {
        User user = TestDataFactory.createValidUser();
        hibernateDAO.save(user);
        assertTrue(hibernateDAO.findUserById(user.getId()).isPresent());

        hibernateDAO.delete(user.getId());

        Optional<User> deleted = hibernateDAO.findUserById(user.getId());
        assertFalse(deleted.isPresent());
    }
}
