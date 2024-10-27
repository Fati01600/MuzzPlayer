package services;

import dat.config.HibernateConfig;
import dat.dtos.UserDTO;
import dat.services.UserService;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserServiceIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.3-alpine")
            .withDatabaseName("test_db")
            .withUsername("postgres")
            .withPassword("postgres");

    private static UserService userService;

    @BeforeAll
    static void setUp() {
        postgresContainer.start();
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
        userService = new UserService(emf);
    }

    @BeforeEach
    void cleanDatabase() {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = HibernateConfig.getEntityManagerFactoryForTest().createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Playlist").executeUpdate();
            em.createQuery("DELETE FROM Song").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Test
    void testCreateUser() {
        UserDTO newUser = new UserDTO();
        newUser.setUsername("testUser");
        newUser.setPassword("password123");

        UserDTO createdUser = userService.createUser(newUser);

        assertNotNull(createdUser);
        assertEquals("testUser", createdUser.getUsername());
    }

    @Test
    void testGetAllUsers() {
        UserDTO newUser1 = new UserDTO();
        newUser1.setUsername("testUser1");
        newUser1.setPassword("password123");
        userService.createUser(newUser1);

        UserDTO newUser2 = new UserDTO();
        newUser2.setUsername("testUser2");
        newUser2.setPassword("password1234");
        userService.createUser(newUser2);

        List<UserDTO> allUsers = userService.getAllUsers();
        assertEquals(2, allUsers.size());
        assertThat(allUsers, hasSize(2));
    }

    @Test
    void testCreateUserWithoutUsername() {
        UserDTO newUser = new UserDTO();
        newUser.setPassword("password123");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(newUser);
        });

        String expectedMessage = "{ status : 400, 'msg': 'Invalid input: Username is required' }";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testCreateUserWithoutPassword() {
        UserDTO newUser = new UserDTO();
        newUser.setUsername("testUserWithoutPassword");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(newUser);
        });

        String expectedMessage = "{ status : 400, 'msg': 'Invalid input: Password is required' }";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void testGetAllUsers_NoContent() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userService.getAllUsers();
        });

        String expectedMessage = "{ status : 404, 'msg': 'No content found' }";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
