package dat.routes;

import dat.controllers.ProfileController;
import dat.controllers.PlaylistController;
import dat.controllers.SongController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MuzzPlayerRoutes {

    private final ProfileController profileController;
    private final PlaylistController playlistController;
    private final SongController songController;

    public MuzzPlayerRoutes(ProfileController profileController, PlaylistController playlistController, SongController songController) {
        this.profileController = profileController;
        this.playlistController = playlistController;
        this.songController = songController;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            path("/profiles", () -> {
                post(profileController::createProfile);
                get(profileController::getAllProfiles);
                get("/{id}", profileController::getProfileById);
                put("/{id}", profileController::updateProfile);
                delete("/{id}", profileController::deleteProfile);

                // Compatibility endpoint
                get("/{id1}/compatibility/{id2}", profileController::getCompatibility);
            });

            path("/playlists", () -> {
                post(playlistController::createPlaylist);
                get(playlistController::getAllPlaylists);
                get("/{id}", playlistController::getPlaylistById);
                put("/{id}", playlistController::updatePlaylist);
                delete("/{id}", playlistController::deletePlaylist);
            });

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
