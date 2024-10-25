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
            return PlaylistDTO.toList(em.createQuery("SELECT p FROM Playlist p", Playlist.class).getResultList());
        }
    }

    public PlaylistDTO getById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Playlist playlist = em.find(Playlist.class, id);
            if (playlist != null) {
                return new PlaylistDTO(playlist);
            }
        }
        return null;
    }

    public PlaylistDTO create(PlaylistDTO playlistDTO, UserDTO profileDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, profileDTO.getUsername());
            Playlist playlist = new Playlist(playlistDTO);
            playlist.setSongs(convertSongDTOsToEntities(playlistDTO.getSongs()));
            playlist.addUser(user);

            em.getTransaction().begin();
            playlist = em.merge(playlist);
            em.getTransaction().commit();

            return new PlaylistDTO(playlist);
        }
    }


    public PlaylistDTO update(int id, PlaylistDTO playlistDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            Playlist playlist = em.find(Playlist.class, id);
            if (playlist != null) {
                em.getTransaction().begin();
                playlist.setName(playlistDTO.getName());
                playlist.setGenre(playlistDTO.getGenre());
                playlist.setMood(playlistDTO.getMood());
                playlist.setSongs(convertSongDTOsToEntities(playlistDTO.getSongs()));
                em.getTransaction().commit();
                return new PlaylistDTO(playlist);
            }
        }
        return null;
    }

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


    public List<PlaylistDTO> createFromList(List<PlaylistDTO> playlistDTOS, UserDTO userDTO) {
        List<PlaylistDTO> playlistDTOList = new ArrayList<>();
        for (int index = 0; index < playlistDTOS.size(); index++) {
            PlaylistDTO newPlaylistDTO = create(playlistDTOS.get(index), userDTO);
            playlistDTOList.add(newPlaylistDTO);
        }
        return playlistDTOList;
    }


    private List<Song> convertSongDTOsToEntities(List<SongDTO> songDTOs) {
        List<Song> songs = new ArrayList<>();
        for (SongDTO songDTO : songDTOs) {
            Song song = new Song(songDTO);
            songs.add(song);
        }
        return songs;
    }
}
