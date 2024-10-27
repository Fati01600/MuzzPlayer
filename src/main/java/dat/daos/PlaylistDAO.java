package dat.daos;

import dat.dtos.PlaylistDTO;
import dat.dtos.SongDTO;
import dat.entities.Playlist;
import dat.entities.Song;
import dat.dtos.UserDTO;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {

    private static PlaylistDAO instance;
    private static EntityManagerFactory emf;

    private PlaylistDAO(EntityManagerFactory _emf) {
        emf = _emf;
    }

    public static PlaylistDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            instance = new PlaylistDAO(_emf);
        }
        return instance;
    }

    public List<PlaylistDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Playlist> playlists = em.createQuery("SELECT p FROM Playlist p", Playlist.class).getResultList();
            return PlaylistDTO.toList(playlists);
        }
    }

    public PlaylistDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Playlist playlist = em.find(Playlist.class, id);
            if (playlist == null) {
                throw new IllegalStateException("{ status : 404, 'msg': 'Resource not found' }");
            }
            return new PlaylistDTO(playlist);
        }
    }

    public PlaylistDTO create(PlaylistDTO playlistDTO, UserDTO userDTO) {
        if (playlistDTO.getName() == null || playlistDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("{ status : 400, 'msg': 'Invalid input: Required fields are missing' }");
        }

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();


            long count = em.createQuery(
                            "SELECT COUNT(p) FROM Playlist p WHERE p.name = :name AND p.user.username = :username", Long.class)
                    .setParameter("name", playlistDTO.getName())
                    .setParameter("username", userDTO.getUsername())
                    .getSingleResult();

            if (count > 0) {
                throw new IllegalStateException("{ status : 409, 'msg': 'Conflict: Duplicate playlist name detected in request' }");
            }

            User user = em.find(User.class, userDTO.getUsername());
            Playlist playlist = new Playlist(playlistDTO);
            playlist.setSongs(convertSongDTOsToEntities(playlistDTO.getSongs()));
            playlist.addUser(user);

            em.persist(playlist);
            em.getTransaction().commit();

            return new PlaylistDTO(playlist);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public PlaylistDTO update(int id, PlaylistDTO playlistDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Playlist playlist = em.find(Playlist.class, id);
            if (playlist == null) {
                throw new IllegalStateException("{ status : 404, 'msg': 'Resource not found' }");
            }
            playlist.setName(playlistDTO.getName());
            playlist.setGenre(playlistDTO.getGenre());
            playlist.setMood(playlistDTO.getMood());
            playlist.setSongs(convertSongDTOsToEntities(playlistDTO.getSongs()));
            em.getTransaction().commit();
            return new PlaylistDTO(playlist);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Playlist playlist = em.find(Playlist.class, id);
            if (playlist == null) {
                throw new IllegalStateException("{ status : 404, 'msg': 'Resource not found' }");
            }
            em.remove(playlist);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<PlaylistDTO> createFromList(List<PlaylistDTO> playlistDTOS, UserDTO userDTO) {
        List<PlaylistDTO> playlistDTOList = new ArrayList<>();
        for (PlaylistDTO playlistDTO : playlistDTOS) {
            playlistDTOList.add(create(playlistDTO, userDTO));
        }
        return playlistDTOList;
    }

    private List<Song> convertSongDTOsToEntities(List<SongDTO> songDTOs) {
        List<Song> songs = new ArrayList<>();
        for (SongDTO songDTO : songDTOs) {
            songs.add(new Song(songDTO));
        }
        return songs;
    }
}
