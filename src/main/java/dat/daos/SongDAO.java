package dat.daos;

import dat.dtos.SongDTO;
import dat.entities.Song;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class SongDAO extends DAO<SongDTO> {

    private static SongDAO instance;
    private static EntityManagerFactory emf;

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
            return SongDTO.toDTOList(query.getResultList());
        }
    }

    @Override
    public SongDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Song song = em.find(Song.class, id);
            if (song != null) {
                return new SongDTO(song);
            }
        }
        return null;
    }

    @Override
    public SongDTO create(SongDTO songDTO) {
        Song song = new Song(songDTO);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(song);
            em.getTransaction().commit();
        }
        return new SongDTO(song);
    }

    @Override
    public SongDTO update(int id, SongDTO songDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            Song song = em.find(Song.class, id);
            if (song == null) {
                // Return null if no song is found
                return null;
            }
            em.getTransaction().begin();
            song.setTitle(songDTO.getTitle());
            song.setArtist(songDTO.getArtist());
            song.setGenre(songDTO.getGenre());
            em.getTransaction().commit();
            return new SongDTO(song);
        }
    }


    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Song song = em.find(Song.class, id);
            if (song != null) {
                em.getTransaction().begin();
                em.remove(song);
                em.getTransaction().commit();
            }
        }
    }
}