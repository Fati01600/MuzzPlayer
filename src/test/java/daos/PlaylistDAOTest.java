package daos;

import dat.config.HibernateConfig;
import dat.daos.PlaylistDAO;
import dat.daos.UserDAO;
import dat.dtos.PlaylistDTO;
import dat.dtos.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class PlaylistDAOTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("test_db")
            .withUsername("postgres")
            .withPassword("postgres");

    private static PlaylistDAO playlistDAO;
    private static UserDAO userDAO;
    private static EntityManagerFactory emf;

    @BeforeAll
    static void setUp() {
        postgresContainer.start();
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        playlistDAO = PlaylistDAO.getInstance(emf);
        userDAO = UserDAO.getInstance(emf);
    }

    @BeforeEach
    void cleanDatabase() {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = emf.createEntityManager();
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
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("securePassword");

        userDAO.create(userDTO);

        PlaylistDTO playlist1 = new PlaylistDTO();
        playlist1.setName("Chill Vibes");

        PlaylistDTO playlist2 = new PlaylistDTO();
        playlist2.setName("Workout Hits");

        List<PlaylistDTO> playlists = List.of(playlist1, playlist2);

        List<PlaylistDTO> createdPlaylists = playlistDAO.createFromList(playlists, userDTO);

        assertNotNull(createdPlaylists);
        assertThat(createdPlaylists, hasSize(2));
        assertThat(createdPlaylists.get(0).getName(), is("Chill Vibes"));
        assertThat(createdPlaylists.get(1).getName(), is("Workout Hits"));
    }

    @Test
    void testGetAllPlaylists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("securePassword");

        userDAO.create(userDTO);

        PlaylistDTO playlist1 = new PlaylistDTO();
        playlist1.setName("Chill Vibes");

        PlaylistDTO playlist2 = new PlaylistDTO();
        playlist2.setName("Workout Hits");

        playlistDAO.createFromList(List.of(playlist1, playlist2), userDTO);

        List<PlaylistDTO> allPlaylists = playlistDAO.getAll();

        assertNotNull(allPlaylists);
        assertThat(allPlaylists, hasSize(2));
    }

    @Test
    void testUpdatePlaylist() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("securePassword");

        userDAO.create(userDTO);

        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setName("Chill Vibes");

        PlaylistDTO createdPlaylist = playlistDAO.create(playlistDTO, userDTO);

        PlaylistDTO updateData = new PlaylistDTO();
        updateData.setName("Chill Vibes Updated");

        PlaylistDTO updatedPlaylist = playlistDAO.update(Math.toIntExact(createdPlaylist.getId()), updateData);

        assertNotNull(updatedPlaylist);
        assertEquals("Chill Vibes Updated", updatedPlaylist.getName());
    }

    @Test
    void testDeletePlaylist() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("securePassword");

        userDAO.create(userDTO);

        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.setName("Chill Vibes");

        PlaylistDTO createdPlaylist = playlistDAO.create(playlistDTO, userDTO);

        playlistDAO.delete(Math.toIntExact(createdPlaylist.getId()));

        List<PlaylistDTO> allPlaylists = playlistDAO.getAll();
        assertThat(allPlaylists, hasSize(0));
    }

    @Test
    void testCreatePlaylist_ThrowsExceptionForDuplicateName() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("securePassword");

        userDAO.create(userDTO);

        PlaylistDTO playlist1 = new PlaylistDTO();
        playlist1.setName("Chill Vibes");

        PlaylistDTO playlist2 = new PlaylistDTO();
        playlist2.setName("Chill Vibes");

        List<PlaylistDTO> playlists = List.of(playlist1, playlist2);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            playlistDAO.createFromList(playlists, userDTO);
        });

        String expectedMessage = "{ status : 409, 'msg': 'Conflict: Duplicate playlist name detected in request' }";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
