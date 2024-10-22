package dat.daos;

import dat.dtos.PlaylistDTO;
import dat.entities.Playlist;
import dat.entities.Song;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.stream.Collectors;

public class PlaylistDAO {

    private static PlaylistDAO instance;
    private static EntityManagerFactory emf;

    private PlaylistDAO() {
//        Singleton pattern - prevent instantiation
    }

    public static PlaylistDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            instance = new PlaylistDAO();
            emf = _emf;
        }
        return instance;
    }

    // Henter alle playlister som PlaylistDTO'er
    public List<PlaylistDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Playlist> query = em.createQuery("SELECT p FROM Playlist p", Playlist.class);
            return PlaylistDTO.toList(query.getResultList());
        }
    }

    // Finder en playliste baseret på ID og returnerer en PlaylistDTO
    public PlaylistDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Playlist playlist = em.find(Playlist.class, id);
            if (playlist != null) {
                return new PlaylistDTO(playlist);
            }
        }
        return null;
    }

    // Opretter en ny playliste baseret på PlaylistDTO
    public PlaylistDTO create(PlaylistDTO playlistDTO) {
        Playlist playlist = new Playlist(playlistDTO);
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(playlist);
            em.getTransaction().commit();
        }
        return new PlaylistDTO(playlist);
    }

    // Opdaterer playlister baseret på deres ID
    public PlaylistDTO update(int id, PlaylistDTO playlistDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            Playlist playlist = em.find(Playlist.class, id);
            if (playlist != null) {
                em.getTransaction().begin();
                playlist.setName(playlistDTO.getName());
                playlist.setSongs(playlistDTO.getSongs().stream()
                        .map(Song::new)
                        .collect(Collectors.toList()));
                em.getTransaction().commit();
                return new PlaylistDTO(playlist);
            }
        }
        return null;
    }

    // Sletter en playliste baseret på deres ID
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Playlist playlist = em.find(Playlist.class, id);
            if (playlist != null) {
                em.getTransaction().begin();
                em.remove(playlist);
                em.getTransaction().commit();
            }
        }
    }
}
