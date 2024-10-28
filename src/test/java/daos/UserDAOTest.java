/*package daos;

import dat.config.HibernateConfig;
import dat.daos.UserDAO;
import dat.dtos.UserDTO;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDAOTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.3-alpine")
            .withDatabaseName("test_db")
            .withUsername("postgres")
            .withPassword("postgres");

    private static UserDAO userDAO;

    @BeforeAll
    static void setUp() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
        userDAO = UserDAO.getInstance(emf);
    }

    @BeforeEach
    void cleanDatabase() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
        var em = emf.createEntityManager();
        var transaction = em.getTransaction();

        try {
            transaction.begin();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Playlist").executeUpdate();
            em.createQuery("DELETE FROM Song").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            em.close();
        }
    }

    @Test
    void testCreateUser() {
        UserDTO newUser = new UserDTO();
        newUser.setUsername("testUser");
        newUser.setPassword("password123");

        UserDTO createdUser = userDAO.create(newUser);

        assertNotNull(createdUser);
        assertEquals("testUser", createdUser.getUsername());
    }

    @Test
    void testCreateUserWithoutUsername_ThrowsException() {
        UserDTO newUser = new UserDTO();
        newUser.setPassword("password123");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userDAO.create(newUser);
        });

        String expectedMessage = "{ status : 400, 'msg': 'Invalid input: Username is required' }";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCreateUserWithoutPassword_ThrowsException() {
        UserDTO newUser = new UserDTO();
        newUser.setUsername("testUserWithoutPassword");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userDAO.create(newUser);
        });

        String expectedMessage = "{ status : 400, 'msg': 'Invalid input: Password is required' }";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetAllUsers() {
        UserDTO newUser1 = new UserDTO();
        newUser1.setUsername("testUser1");
        newUser1.setPassword("password123");
        userDAO.create(newUser1);

        UserDTO newUser2 = new UserDTO();
        newUser2.setUsername("testUser2");
        newUser2.setPassword("password456");
        userDAO.create(newUser2);

        List<UserDTO> allUsers = userDAO.getAll();
        assertEquals(2, allUsers.size());
        assertThat(allUsers, hasSize(2));
    }

    @Test
    void testGetUserByUsername_ThrowsExceptionForNonExistentUser() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userDAO.getByUserName("nonExistentUser");
        });

        String expectedMessage = "{ status : 404, 'msg': 'Resource not found' }";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetUserByUsername() {
        UserDTO newUser = new UserDTO();
        newUser.setUsername("testUser3");
        newUser.setPassword("password123");

        UserDTO createdUser = userDAO.create(newUser);

        UserDTO foundUser = userDAO.getByUserName(createdUser.getUsername());
        assertNotNull(foundUser);
        assertEquals("testUser3", foundUser.getUsername());
    }

    @Test
    void testUpdateUser() {
        UserDTO newUser = new UserDTO();
        newUser.setUsername("testUser5");
        newUser.setPassword("password123");

        UserDTO createdUser = userDAO.create(newUser);
        assertNotNull(createdUser);
        String newPassword = "newPassword123";
        createdUser.setPassword(newPassword);
        userDAO.update("testUser5", createdUser);

        UserDTO updatedUser = userDAO.getByUserName("testUser5");

        assertNotNull(updatedUser);
        assertTrue(BCrypt.checkpw(newPassword, updatedUser.getPassword()), "Password should be updated to newPassword123");//Jeg bruger BCrypt til at se at passworded er updateret
    }

    @Test
    void testCalculateCompatibility() {
        UserDTO user1 = new UserDTO();
        user1.setUsername("userOne");
        user1.setPassword("password123");
        userDAO.create(user1);

        UserDTO user2 = new UserDTO();
        user2.setUsername("userTwo");
        user2.setPassword("password123");
        userDAO.create(user2);

        double compatibility = userDAO.calculateCompatibility("userOne", "userTwo");
        assertEquals(0.0, compatibility);
    }
}*/
