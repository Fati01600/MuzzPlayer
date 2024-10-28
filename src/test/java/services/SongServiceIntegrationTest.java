/*package services;

import dat.config.HibernateConfig;
import dat.dtos.SongDTO;
import dat.services.SongService;
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
public class SongServiceIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.3-alpine")
            .withDatabaseName("test_db")
            .withUsername("postgres")
            .withPassword("postgres");

    private static SongService songService;

    @BeforeAll
    static void setUp() {
        postgresContainer.start();
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
        songService = new SongService(emf);
    }

    @BeforeEach
    void cleanDatabase() {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = HibernateConfig.getEntityManagerFactoryForTest().createEntityManager();
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

        SongDTO createdSong = songService.createSong(newSong);

        assertNotNull(createdSong);
        assertEquals("Kvinde Min", createdSong.getTitle());
        assertEquals("Kim Larsen", createdSong.getArtist());
    }

    @Test
    void testCreateSongWithoutTitle_ThrowsException() {
        SongDTO newSong = new SongDTO();
        newSong.setArtist("Kim Larsen");
        newSong.setGenre("Rock");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            songService.createSong(newSong);
        });

        String expectedMessage = "{ status : 400, 'msg': 'Invalid input: Title is required' }";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetAllSongs() {
        SongDTO song1 = new SongDTO("Kvinde Min", "Kim Larsen", "Rock");
        SongDTO song2 = new SongDTO("Papirsklip", "Kim Larsen", "Folk");

        songService.createSong(song1);
        songService.createSong(song2);

        List<SongDTO> allSongs = songService.getAllSongs();

        assertNotNull(allSongs);
        assertThat(allSongs, hasSize(2));
        assertThat(allSongs.get(0).getTitle(), is("Kvinde Min"));
        assertThat(allSongs.get(1).getTitle(), is("Papirsklip"));
    }

    @Test
    void testUpdateSong() {
        SongDTO song = new SongDTO("Kvinde Min", "Kim Larsen", "Rock");
        SongDTO createdSong = songService.createSong(song);

        SongDTO updateData = new SongDTO();
        updateData.setTitle("Kvinde Min (Live)");
        updateData.setArtist("Kim Larsen");
        updateData.setGenre("Rock");

        SongDTO updatedSong = songService.updateSong(Math.toIntExact(createdSong.getId()), updateData);

        assertNotNull(updatedSong);
        assertEquals("Kvinde Min (Live)", updatedSong.getTitle());
    }

    @Test
    void testDeleteSong() {
        SongDTO song = new SongDTO("Kvinde Min", "Kim Larsen", "Rock");
        SongDTO createdSong = songService.createSong(song);

        songService.deleteSong(Math.toIntExact(createdSong.getId()));

        List<SongDTO> allSongs = songService.getAllSongs();

        assertTrue(allSongs.isEmpty(), "List of songs should be empty after deletion");
    }


    @Test
    void testGetById_ThrowsExceptionWhenSongNotFound() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            songService.getSongById(999);
        });

        String expectedMessage = "{ status : 404, 'msg': 'Resource not found' }";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}*/
