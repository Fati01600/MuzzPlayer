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
        for (PlaylistDTO playlistDTO : playlistDTOs) {
            if (playlistDTO.getName() == null || playlistDTO.getName().isEmpty()) {
                throw new IllegalArgumentException("{ status : 400, 'msg': 'Invalid input: Playlist name cannot be null or empty' }"); // e2
            }
            long count = playlistDTOs.stream()
                    .filter(p -> p.getName().equals(playlistDTO.getName()))
                    .count();
            if (count > 1) {
                throw new IllegalStateException("{ status : 409, 'msg': 'Conflict: Duplicate playlist name detected in request' }"); // e3
            }
        }
        return playlistDAO.createFromList(playlistDTOs, user);
    }

    public PlaylistDTO updatePlaylist(int id, PlaylistDTO playlistDTO) {
        return playlistDAO.update(id, playlistDTO);
    }

    public void deletePlaylist(int id) {
        playlistDAO.delete(id);
    }
}
