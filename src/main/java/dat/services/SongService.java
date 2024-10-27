package dat.services;

import dat.daos.SongDAO;
import dat.dtos.SongDTO;
import jakarta.persistence.EntityManagerFactory;

import java.util.Collections;
import java.util.List;

public class SongService {

    private final SongDAO songDAO;

    public SongService(EntityManagerFactory emf) {
        this.songDAO = SongDAO.getInstance(emf);
    }


    public List<SongDTO> getAllSongs() {
        List<SongDTO> songs = songDAO.getAll();
        if (songs.isEmpty()) {
            return Collections.emptyList();
        }
        return songs;
    }

    public SongDTO getSongById(int id) {
        SongDTO song = songDAO.getById(id);
        if (song == null) {
            throw new IllegalStateException("{ status : 404, 'msg': 'Resource not found' }"); // e1
        }
        return song;
    }

    public SongDTO createSong(SongDTO songDTO) {
        if (songDTO.getTitle() == null || songDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("{ status : 400, 'msg': 'Invalid input: Title is required' }"); // e2
        }
        if (songDTO.getArtist() == null || songDTO.getArtist().isEmpty()) {
            throw new IllegalArgumentException("{ status : 400, 'msg': 'Invalid input: Artist is required' }"); // e2
        }
        return songDAO.create(songDTO);
    }


    public SongDTO updateSong(int id, SongDTO songDTO) {
        SongDTO song = songDAO.getById(id);
        if (song == null) {
            throw new IllegalStateException("{ status : 404, 'msg': 'Resource not found' }"); // e1
        }
        return songDAO.update(id, songDTO);
    }

    public void deleteSong(int id) {
        SongDTO song = songDAO.getById(id);
        if (song == null) {
            throw new IllegalStateException("{ status : 404, 'msg': 'Resource not found' }"); // e1
        }
        songDAO.delete(id);
    }
}