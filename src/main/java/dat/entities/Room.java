package dat.entities;

import dat.dtos.HotelDTO;
import dat.dtos.RoomDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="hotel_id", nullable = false)
    private Integer hotelId;
    @Column(name="number", nullable = false)
    private String number;
    @Column(name="price", nullable = false)
    private Double price;

    public Room (RoomDTO roomDTO){
        this.id = roomDTO.getId();
        this.hotelId = roomDTO.getHotelId();
        this.number = roomDTO.getNumber();
        this.price = roomDTO.getPrice();
    }
}
