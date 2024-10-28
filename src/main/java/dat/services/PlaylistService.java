package dat.services;

import dat.daos.PlaylistDAO;
import dat.dtos.PlaylistDTO;
import dat.dtos.UserDTO;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PlaylistService {

    private final PlaylistDAO playlistDAO;

    public PlaylistService(EntityManagerFactory emf) {
        this.playlistDAO = PlaylistDAO.getInstance(emf);
    }

    public List<PlaylistDTO> getAllPlaylists() {
        return playlistDAO.getAll();
    }

    public PlaylistDTO getPlaylistById(int id) {
        return playlistDAO.getById(id);
    }


    public List<PlaylistDTO> createMultiplePlaylists(List<PlaylistDTO> playlistDTOs, UserDTO user) {
        return playlistDAO.createFromList(playlistDTOs, user);
    }

    public PlaylistDTO updatePlaylist(int id, PlaylistDTO playlistDTO) {
        return playlistDAO.update(id, playlistDTO);
    }

    public void deletePlaylist(int id) {
        playlistDAO.delete(id);
    }
}
