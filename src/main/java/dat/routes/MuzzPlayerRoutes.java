package dat.routes;

import dat.controllers.UserController;
import dat.controllers.PlaylistController;
import dat.controllers.SongController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MuzzPlayerRoutes {

    private final UserController userController;
    private final PlaylistController playlistController;
    private final SongController songController;

    public MuzzPlayerRoutes(UserController userController, PlaylistController playlistController, SongController songController) {
        this.userController = userController;
        this.playlistController = playlistController;
        this.songController = songController;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            path("/users", () -> {
                post(userController::createUser);
                get(userController::getAllUsers);
                get("/{id}", userController::getUserById);
                put("/{id}", userController::updateUser);
                delete("/{id}", userController::deleteUser);

                // Compatibility endpoint
                get("/{id1}/compatibility/{id2}", userController::getCompatibility);
            });

            // Playlist endpoints
            path("/playlists", () -> {
                post(playlistController::createPlaylist);
                get(playlistController::getAllPlaylists);
                get("/{id}", playlistController::getPlaylistById);
                put("/{id}", playlistController::updatePlaylist);
                delete("/{id}", playlistController::deletePlaylist);
            });

            // Song endpoints
            path("/songs", () -> {
                post(songController::createSong);
                get(songController::getAllSongs);
                get("/{id}", songController::getSongById);
                put("/{id}", songController::updateSong);
                delete("/{id}", songController::deleteSong);
            });
        };
    }
}
