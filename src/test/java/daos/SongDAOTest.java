package daos;

import dat.config.HibernateConfig;
import dat.daos.SongDAO;
import dat.dtos.SongDTO;
import dat.entities.Song;
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
public class SongDAOTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.3-alpine")
            .withDatabaseName("test_db")
            .withUsername("postgres")
            .withPassword("postgres");

    private static SongDAO songDAO;
    private static EntityManagerFactory emf;

    @BeforeAll
    static void setUp() {
        postgresContainer.start();
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        songDAO = SongDAO.getInstance(emf);
    }

    @BeforeEach
    void cleanDatabase() {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            em.createQuery("DELETE FROM Song").executeUpdate();
            em.createQuery("DELETE FROM Playlist").executeUpdate();
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
    void testCreateSong() {
        SongDTO newSong = new SongDTO("Kvinde Min", "Kim Larsen", "Rock");
        SongDTO createdSong = songDAO.create(newSong);

        assertNotNull(createdSong);
        assertEquals("Kvinde Min", createdSong.getTitle());
        assertEquals("Kim Larsen", createdSong.getArtist());
    }

    @Test
    void testCreateSongWithoutTitle_ThrowsException() {
        SongDTO newSong = new SongDTO(null, "Kim Larsen", "Rock");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            songDAO.create(newSong);
        });

        String expectedMessage = "{ status : 400, 'msg': 'Invalid input: Required fields are missing' }";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void testGetAllSongs() {
        SongDTO song1 = new SongDTO("Kvinde Min", "Kim Larsen", "Rock");
        SongDTO song2 = new SongDTO("Papirsklip", "Kim Larsen", "Folk");

        songDAO.create(song1);
        songDAO.create(song2);

        List<SongDTO> allSongs = songDAO.getAll();

        assertNotNull(allSongs);
        assertThat(allSongs, hasSize(2));
        assertThat(allSongs.get(0).getTitle(), is("Kvinde Min"));
        assertThat(allSongs.get(1).getTitle(), is("Papirsklip"));
    }

    @Test
    void testUpdateSong() {
        SongDTO song = new SongDTO("Kvinde Min", "Kim Larsen", "Rock");
        SongDTO createdSong = songDAO.create(song);

        assertNotNull(createdSong, "The song creation failed, createdSong is null.");

        createdSong.setTitle("Kvinde Min (Live)");
        createdSong.setArtist("Kim Larsen");
        createdSong.setGenre("Rock");

        SongDTO updatedSong = songDAO.update((int) createdSong.getId(), createdSong);

        assertNotNull(updatedSong, "The song update failed, updatedSong is null.");
        assertEquals("Kvinde Min (Live)", updatedSong.getTitle());
    }

    @Test
    void testDeleteSong() {
        SongDTO song = new SongDTO("Kvinde Min", "Kim Larsen", "Rock");
        SongDTO createdSong = songDAO.create(song);

        songDAO.delete(Math.toIntExact(createdSong.getId()));

        List<SongDTO> allSongs = songDAO.getAll();
        assertThat(allSongs, hasSize(0));
    }

    @Test
    void testGetSongById_ThrowsExceptionForNonExistentSong() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            songDAO.getById(100);
        });

        String expectedMessage = "{ status : 404, 'msg': 'Resource not found' }";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
