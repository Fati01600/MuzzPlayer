package dat.controllers;

import dat.daos.SongDAO;
import dat.dtos.SongDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SongController {

    private final EntityManagerFactory emf;
    SongDAO songDAO;

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    public SongController(EntityManagerFactory emf) {
        this.emf = emf;
        this.songDAO = SongDAO.getInstance(emf);
    }

    public void getAllSongs(Context ctx) {
        List<SongDTO> songs = songDAO.getAll();
        if (songs != null) {
            ctx.json(songs);
        } else {
            ctx.status(404);
        }
    }

    public void getSongById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        SongDTO song = songDAO.getById(id);
        if (song != null) {
            ctx.json(song);
        } else {
            ctx.status(404).result("Song not found");
        }
    }

    public void createSong(Context ctx) {
        SongDTO songDTO = ctx.bodyAsClass(SongDTO.class);
        SongDTO newSongDTO = songDAO.create(songDTO);
        ctx.status(HttpStatus.CREATED).json(newSongDTO);
    }

    public void updateSong(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        SongDTO songDTO = ctx.bodyAsClass(SongDTO.class);
        SongDTO updatedSongDTO = songDAO.update(id, songDTO);
        if (updatedSongDTO != null) {
            ctx.status(HttpStatus.OK).json(updatedSongDTO);
        } else {
            ctx.status(404).result("Song not found");
        }
    }

    public void deleteSong(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        songDAO.delete(id);
        ctx.result("Deleted song with ID: " + id);
    }
}
