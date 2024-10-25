package dat.entities;

import dat.dtos.PlaylistDTO;
import dat.security.entities.User;
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "playlist_song",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id"))
    private List<Song> songs = new ArrayList<>();

    // Constructor to map PlaylistDTO to Playlist entity
    public Playlist(PlaylistDTO playlistDTO) {
        this.name = playlistDTO.getName();
        this.genre = playlistDTO.getGenre();
        this.mood = playlistDTO.getMood();
        this.songs = playlistDTO.getSongs() != null ? playlistDTO.getSongs().stream()
                .map(Song::new)
                .toList() : new ArrayList<>();
    }
    public void addUser(User user){
        this.user = user;
    }
}
