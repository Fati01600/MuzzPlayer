package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.entities.Song;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)

public class SongDTO {
    private Long id;
    private String title;
    private String artist;
    private String genre;

    public SongDTO(dat.entities.Song song) {
        this.id = song.getId();
        this.title = song.getTitle();
        this.artist = song.getArtist();
        this.genre = song.getGenre();
    }
    public static List<SongDTO> toDTOList(List<Song>songs){
        return songs.stream().map(SongDTO::new).toList();
    }
}
