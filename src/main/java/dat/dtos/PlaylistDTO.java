package dat.dtos;

import dat.entities.Playlist;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlaylistDTO {
    private Long id;
    private String name;
    private String genre;
    private String mood;
    private List<SongDTO> songs = new ArrayList<>();

    public PlaylistDTO(Playlist playlist) {
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.genre = playlist.getGenre();
        this.mood = playlist.getMood();
        this.songs = SongDTO.toDTOList(playlist.getSongs());
    }

    public static List<PlaylistDTO> toList(List<Playlist> playlists) {
        return playlists.stream()
                .map(PlaylistDTO::new)
                .toList();
    }
}
