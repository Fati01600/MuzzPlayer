package dat.routes;

import dat.controllers.UserController;
import dat.controllers.PlaylistController;
import dat.controllers.SongController;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MuzzPlayerRoutes {

    private final UserController userController;
    private final PlaylistController playlistController;
    private final SongController songController;
    private final SecurityController securityController = SecurityController.getInstance();

    public MuzzPlayerRoutes(UserController userController, PlaylistController playlistController, SongController songController) {
        this.userController = userController;
        this.playlistController = playlistController;
        this.songController = songController;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            path("/users", () -> {
                // Slette-bruger-ruten
                delete("/{username}", userController::deleteUser, Role.ADMIN);

                // Compatibility endpoint
                get("/{id1}/compatibility/{id2}", userController::getCompatibility, Role.USER);
            });

            // Playlist endpoints
            path("/playlists", () -> {
                post(playlistController::createPlaylist, Role.USER);
                get(playlistController::getAllPlaylists, Role.USER);
                get("/{id}", playlistController::getPlaylistById, Role.USER);
                put("/{id}", playlistController::updatePlaylist, Role.USER);
                delete("/{id}", playlistController::deletePlaylist, Role.USER);
            });

            // Song endpoints
            path("/songs", () -> {
                post(songController::createSong, Role.ADMIN);
                get(songController::getAllSongs, Role.ANYONE);
                get("/{id}", songController::getSongById, Role.ANYONE);
                put("/{id}", songController::updateSong, Role.ADMIN);
                delete("/{id}", songController::deleteSong, Role.ADMIN);
            });
        };
    }
}