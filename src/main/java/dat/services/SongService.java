package dat.services;

import dat.daos.SongDAO;
import dat.dtos.SongDTO;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class SongService {

    private final SongDAO songDAO;

    public SongService(EntityManagerFactory emf) {
        this.songDAO = SongDAO.getInstance(emf);
    }

    public List<SongDTO> getAllSongs() {
        return songDAO.getAll();
    }

    public SongDTO getSongById(int id) {
        return songDAO.getById(id);
    }

    public SongDTO createSong(SongDTO songDTO) {
        return songDAO.create(songDTO);
    }

    public SongDTO updateSong(int id, SongDTO songDTO) {
        return songDAO.update(id, songDTO);
    }

    public void deleteSong(int id) {
        songDAO.delete(id);
    }
}
