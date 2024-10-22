package dat.entities;

import dat.dtos.ProfileDTO;
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
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private List<Playlist> playlists = new ArrayList<>();

    public Profile(ProfileDTO userDTO) {
        this.id = userDTO.getId();
        this.name = userDTO.getName();
        this.playlists = userDTO.getPlaylists() != null ? userDTO.getPlaylists().stream()
                .map(Playlist::new)
                .toList() : new ArrayList<>();
    }
}
