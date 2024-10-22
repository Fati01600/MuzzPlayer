package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.entities.Hotel;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelDTO {

    private Integer id;
    private String name;
    private String address;
    private List<RoomDTO> rooms = new ArrayList<>(); // Initialiserer rooms som tom liste

    public HotelDTO(Hotel hotel){
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.address = hotel.getAddress();
        this.rooms = hotel.getRooms() != null ? hotel.getRooms().stream()
                .map(RoomDTO::new)
                .toList() : new ArrayList<>(); // Håndterer null ved at returnere en tom liste
    }

    public static List<HotelDTO> toDTOList(List<Hotel> hotels){

        return hotels.stream().map(HotelDTO::new).toList();
    }
}
