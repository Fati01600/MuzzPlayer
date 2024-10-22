package dat.entities;

import dat.dtos.PlaylistDTO;
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
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "genre")
    private String genre;

    @Column(name = "mood")
    private String mood;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "playlist_song",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id"))
    private List<Song> songs = new ArrayList<>();

    public Playlist(PlaylistDTO playlistDTO) {
        this.id = playlistDTO.getId();
        this.name = playlistDTO.getName();
        this.genre = playlistDTO.getGenre();
        this.mood = playlistDTO.getMood();
        this.songs = playlistDTO.getSongs() != null ? playlistDTO.getSongs().stream()
                .map(Song::new)
                .toList() : new ArrayList<>();
    }
}
