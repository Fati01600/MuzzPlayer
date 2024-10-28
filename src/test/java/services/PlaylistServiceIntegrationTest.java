/*package services;

import dat.config.HibernateConfig;
import dat.dtos.PlaylistDTO;
import dat.dtos.UserDTO;
import dat.services.PlaylistService;
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
public class PlaylistServiceIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("test_db")
            .withUsername("postgres")
            .withPassword("postgres");

    private static PlaylistService playlistService;
    private static UserService userService;

    @BeforeAll
    static void setUp() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
        playlistService = new PlaylistService(emf);
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
            em.createQuery("DELETE FROM Playlist").executeUpdate();
            em.createQuery("DELETE FROM Song").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
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
    void testCreateMultiplePlaylists() {
        UserDTO user = new UserDTO();
        user.setUsername("testUser");
        user.setPassword("securePassword");

        userService.createUser(user);

        PlaylistDTO playlist1 = new PlaylistDTO();
        playlist1.setName("Chill Vibes");

        PlaylistDTO playlist2 = new PlaylistDTO();
        playlist2.setName("Workout Hits");

        List<PlaylistDTO> playlists = List.of(playlist1, playlist2);

        List<PlaylistDTO> createdPlaylists = playlistService.createMultiplePlaylists(playlists, user);

        assertNotNull(createdPlaylists);
        assertThat(createdPlaylists, hasSize(2));
        assertThat(createdPlaylists.get(0).getName(), is("Chill Vibes"));
        assertThat(createdPlaylists.get(1).getName(), is("Workout Hits"));
    }

    @Test
    void testCreateMultiplePlaylistsWithDuplicateName_ThrowsException() {
        UserDTO user = new UserDTO();
        user.setUsername("testUser");
        user.setPassword("securePassword");

        userService.createUser(user);

        PlaylistDTO playlist1 = new PlaylistDTO();
        playlist1.setName("Chill Vibes");

        PlaylistDTO playlist2 = new PlaylistDTO();
        playlist2.setName("Chill Vibes"); // Duplicate name

        List<PlaylistDTO> playlists = List.of(playlist1, playlist2);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            playlistService.createMultiplePlaylists(playlists, user);
        });

        String expectedMessage = "{ status : 409, 'msg': 'Conflict: Duplicate playlist name detected in request' }";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCreatePlaylistWithoutName_ThrowsException() {
        UserDTO user = new UserDTO();
        user.setUsername("testUser");
        user.setPassword("securePassword");

        userService.createUser(user);

        PlaylistDTO playlist1 = new PlaylistDTO();
        playlist1.setName("");

        List<PlaylistDTO> playlists = List.of(playlist1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            playlistService.createMultiplePlaylists(playlists, user);
        });

        String expectedMessage = "{ status : 400, 'msg': 'Invalid input: Playlist name cannot be null or empty' }";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}*/
