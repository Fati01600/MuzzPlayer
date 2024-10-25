package dat.controllers;

import dat.dtos.PlaylistDTO;
import dat.dtos.UserDTO;
import dat.services.PlaylistService;
import dat.services.UserService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;

public class PlaylistController {

    private final PlaylistService playlistService;
    private final UserService userService;

    public PlaylistController(PlaylistService playlistService, UserService userService) {
        this.userService = userService;
        this.playlistService = playlistService;
    }

    public void getAllPlaylists(Context ctx) {
        List<PlaylistDTO> playlists = playlistService.getAllPlaylists();
        if (playlists != null) {
            ctx.json(playlists);
        } else {
            ctx.status(404);
        }
    }

    public void getPlaylistById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        PlaylistDTO playlist = playlistService.getPlaylistById(id);
        if (playlist != null) {
            ctx.json(playlist);
        } else {
            ctx.status(404).result("Playlist not found");
        }
    }

    public void createPlaylist(Context ctx) {
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
        List<PlaylistDTO> newPlaylistDTOs = playlistService.createMultiplePlaylists(userDTO.getPlaylists(), userDTO);
        ctx.status(HttpStatus.CREATED).json(newPlaylistDTOs);
    }

    public void updatePlaylist(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        PlaylistDTO playlistDTO = ctx.bodyAsClass(PlaylistDTO.class);
        PlaylistDTO updatedPlaylistDTO = playlistService.updatePlaylist(id, playlistDTO);
        if (updatedPlaylistDTO != null) {
            ctx.status(HttpStatus.OK).json(updatedPlaylistDTO);
        } else {
            ctx.status(404).result("Playlist not found");
        }
    }

    public void deletePlaylist(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        playlistService.deletePlaylist(id);
        ctx.result("Deleted playlist with ID: " + id);
    }
}
