package dat.entities;

import dat.dtos.SongDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "artist", nullable = false)
    private String artist;

    @Column(name = "genre")
    private String genre;

    @ManyToMany(mappedBy = "songs")
    private List<Playlist> playlists = new ArrayList<>();

    public Song(SongDTO songDTO) {
        this.id = songDTO.getId();
        this.title = songDTO.getTitle();
        this.artist = songDTO.getArtist();
        this.genre = songDTO.getGenre();
    }
}