package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dat.entities.Room;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomDTO {

    private Integer id;
    private Integer hotelId;  // Reference to Hotel
    private String number;
    private Double price;

    public RoomDTO(Room room) {
        this.id = room.getId();
        this.hotelId = room.getHotelId();
        this.number = room.getNumber();
        this.price = room.getPrice();
    }

    public static List<RoomDTO> toDTOList(List<Room> rooms){
        return rooms.stream().map(RoomDTO::new).toList();
    }

}
