package dat.daos;

import dat.dtos.SongDTO;
import dat.entities.Song;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class SongDAO extends DAO<SongDTO> {

    private static SongDAO instance;
    private static EntityManagerFactory emf;
    private static final Logger logger = LoggerFactory.getLogger(SongDAO.class);

    private SongDAO() {
    }

    public static SongDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            instance = new SongDAO();
            emf = _emf;
        }
        return instance;
    }

    @Override
    public List<SongDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Song> query = em.createQuery("SELECT s FROM Song s", Song.class);
            List<Song> songs = query.getResultList();
            if (songs.isEmpty()) {
                return Collections.emptyList();
            }
            return SongDTO.toDTOList(songs);
        }
    }

    @Override
    public SongDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Song song = em.find(Song.class, id);
            if (song == null) {
                throw new IllegalStateException("{ status : 404, 'msg': 'Resource not found' }"); // e1
            }
            return new SongDTO(song);
        }
    }

    public SongDTO create(SongDTO songDTO) {
        if (songDTO == null || songDTO.getTitle() == null || songDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("{ status : 400, 'msg': 'Invalid input: Required fields are missing' }");
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        Song song = new Song(songDTO);

        try {
            tx.begin();
            song = em.merge(song);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        } finally {
            em.close();
        }

        return new SongDTO(song);
    }

    @Override
    public SongDTO update(int id, SongDTO songDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Song song = em.find(Song.class, id);
            if (song == null) {
                throw new IllegalStateException("{ status : 404, 'msg': 'Resource not found' }"); // e1
            }
            em.getTransaction().begin();
            song.setTitle(songDTO.getTitle());
            song.setArtist(songDTO.getArtist());
            song.setGenre(songDTO.getGenre());
            song = em.merge(song);
            em.getTransaction().commit();
            logger.info("Song updated: " + song.getTitle());
            return new SongDTO(song);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Song song = em.find(Song.class, id);
            if (song == null) {
                throw new IllegalStateException("{ status : 404, 'msg': 'Resource not found' }"); // e1
            }
            em.getTransaction().begin();
            em.remove(song);
            em.getTransaction().commit();
            logger.info("Song deleted with ID: " + id);
        } finally {
            em.close();
        }
    }
}
