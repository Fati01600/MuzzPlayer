package dat.controllers;

import dat.daos.PlaylistDAO;
import dat.dtos.PlaylistDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PlaylistController {

    private final EntityManagerFactory emf;
    PlaylistDAO playlistDAO;

    private static final Logger logger = LoggerFactory.getLogger(PlaylistController.class);

    public PlaylistController(EntityManagerFactory emf) {
        this.emf = emf;
        this.playlistDAO = PlaylistDAO.getInstance(emf);
    }

    public void getAllPlaylists(Context ctx) {
        List<PlaylistDTO> playlists = playlistDAO.getAll();
        if (playlists != null) {
            ctx.json(playlists);
        } else {
            ctx.status(404);
        }
    }

    public void getPlaylistById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        PlaylistDTO playlist = playlistDAO.getById(id);
        if (playlist != null) {
            ctx.json(playlist);
        } else {
            ctx.status(404).result("Playlist not found");
        }
    }

    public void createPlaylist(Context ctx) {
        PlaylistDTO playlistDTO = ctx.bodyAsClass(PlaylistDTO.class);
        PlaylistDTO newPlaylistDTO = playlistDAO.create(playlistDTO);
        ctx.status(HttpStatus.CREATED).json(newPlaylistDTO);
    }

    public void updatePlaylist(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        PlaylistDTO playlistDTO = ctx.bodyAsClass(PlaylistDTO.class);
        PlaylistDTO updatedPlaylistDTO = playlistDAO.update(id, playlistDTO);
        if (updatedPlaylistDTO != null) {
            ctx.status(HttpStatus.OK).json(updatedPlaylistDTO);
        } else {
            ctx.status(404).result("Playlist not found");
        }
    }

    public void deletePlaylist(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        playlistDAO.delete(id);
        ctx.result("Deleted playlist with ID: " + id);
    }
}
