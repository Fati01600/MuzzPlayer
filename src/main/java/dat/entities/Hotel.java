package dat.entities;

import dat.dtos.HotelDTO;
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
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", length = 100, nullable = false)
    private String name;

    @Column(name="address", length = 100, nullable = false)
    private String address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hotel_id")
    private List<Room> rooms = new ArrayList<>(); // Initialiserer rooms som tom liste

    public Hotel(HotelDTO hotelDTO){
        this.id = hotelDTO.getId();
        this.name = hotelDTO.getName();
        this.address = hotelDTO.getAddress();
        this.rooms = hotelDTO.getRooms() != null ? hotelDTO.getRooms().stream()
                .map(Room::new)
                .toList() : new ArrayList<>(); // Håndterer null
    }
}
