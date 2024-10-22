package dat.dtos;

import dat.entities.Profile;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProfileDTO {
    private Long id;
    private String name;
    private List<PlaylistDTO> playlists;

    public ProfileDTO(Profile profile) {
        this.id = profile.getId();
        this.name = profile.getName();
        this.playlists = profile.getPlaylists() != null ? profile.getPlaylists().stream()
                .map(PlaylistDTO::new)
                .toList() : new ArrayList<>();
    }

    public static List<ProfileDTO> toDTOList(List<Profile> profiles) {
        return profiles.stream().map(ProfileDTO::new).toList();
    }
}
