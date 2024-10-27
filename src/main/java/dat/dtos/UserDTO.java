package dat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import dat.entities.Playlist;
import dat.security.entities.User;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO {

    @JsonProperty("playlists")
    private List<PlaylistDTO> playlists;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("roles")
    private Set<String> roles = new HashSet<>();


    public UserDTO(User user) {
        this.playlists = PlaylistDTO.toList(user.getPlaylists());
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = new HashSet<>();
    }

    public UserDTO(List<PlaylistDTO> playlists, String username) {
        this.playlists = playlists;
        this.username = username;
        this.password = "";
        this.roles = new HashSet<>();
    }


    public static List<UserDTO> toDTOList(List<User> users) {
        return users.stream().map(UserDTO::new).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO dto = (UserDTO) o;
        return Objects.equals(username, dto.username) && Objects.equals(roles, dto.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, roles);
    }
}
