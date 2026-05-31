package integration;

import com.hibernatehw.model.User;
import com.hibernatehw.util.TransactionHelper;
import fixtures.TestDataFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionHelperIntegrationTest {

    private static TransactionHelper transactionHelper;
    private User testUser;

    @BeforeAll
    static void init() {
        transactionHelper = new TransactionHelper(TestContainersConfig.sessionFactory);
    }

    @BeforeEach
    void setUp() {
        testUser = TestDataFactory.createValidUser();
    }

    @AfterEach
    void cleanUp() {
        transactionHelper.executeInTransaction(session -> {
            session.createMutationQuery("DELETE FROM User").executeUpdate();
        });
    }

    @Test
    void executeInTransaction_ShouldCommitChanges() {
        transactionHelper.executeInTransaction(session -> {
            session.persist(testUser);
        });

        assertNotNull(testUser.getId());

        try (Session session = TestContainersConfig.sessionFactory.openSession()) {
            User found = session.get(User.class, testUser.getId());
            assertNotNull(found);
            assertEquals(testUser.getName(), found.getName());
            assertEquals(testUser.getEmail(), found.getEmail());
            assertEquals(testUser.getAge(), found.getAge());
        }
    }

    @Test
    void executeInTransaction_OnException_ShouldRollback() {
        assertThrows(RuntimeException.class, () -> {
            transactionHelper.executeInTransaction(session -> {
                session.persist(testUser);
                throw new RuntimeException("Test exception");
            });
        });
        try (Session session = TestContainersConfig.sessionFactory.openSession()) {
            User found = session.createQuery("FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", testUser.getEmail())
                    .uniqueResult();
            assertNull(found, "Пользователь не должен быть в БД после rollback");
        }
    }
}
