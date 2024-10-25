package dat.controllers;

import dat.dtos.SongDTO;
import dat.services.SongService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;

public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    public void getAllSongs(Context ctx) {
        List<SongDTO> songs = songService.getAllSongs();
        if (songs != null) {
            ctx.json(songs);
        } else {
            ctx.status(404);
        }
    }

    public void getSongById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        SongDTO song = songService.getSongById(id);
        if (song != null) {
            ctx.json(song);
        } else {
            ctx.status(404).result("Song not found");
        }
    }

    public void createSong(Context ctx) {
        SongDTO songDTO = ctx.bodyAsClass(SongDTO.class);
        SongDTO newSongDTO = songService.createSong(songDTO);
        ctx.status(HttpStatus.CREATED).json(newSongDTO);
    }

    public void updateSong(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        SongDTO songDTO = ctx.bodyAsClass(SongDTO.class);
        SongDTO updatedSongDTO = songService.updateSong(id, songDTO);
        if (updatedSongDTO != null) {
            ctx.status(HttpStatus.OK).json(updatedSongDTO);
        } else {
            ctx.status(404).result("Song not found");
        }
    }

    public void deleteSong(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        songService.deleteSong(id);
        ctx.result("Deleted song with ID: " + id);
    }
}
