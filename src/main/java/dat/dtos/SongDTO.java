package dat.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import dat.entities.Song;
import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SongDTO {
    @JsonProperty("id")
    private long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("artist")
    private String artist;

    @JsonProperty("genre")
    private String genre;

    public SongDTO(Song song) {
        this.id = song.getId();
        this.title = song.getTitle();
        this.artist = song.getArtist();
        this.genre = song.getGenre();
    }
    public SongDTO(String title, String artist, String genre){
        this.title = title;
        this.artist = artist;
        this.genre = genre;
    }

    public static List<SongDTO> toDTOList(List<Song> songs){
        return songs.stream().map(SongDTO::new).toList();
    }
}
