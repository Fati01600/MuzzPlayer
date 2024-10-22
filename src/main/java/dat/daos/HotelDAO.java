package dat.daos;

import dat.dtos.HotelDTO;
import dat.dtos.RoomDTO;
import dat.entities.Hotel;
import dat.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

public class HotelDAO extends DAO<HotelDTO> {

    private static HotelDAO instance;
    private static EntityManagerFactory emf;

    private HotelDAO() {
        // private constructor
    }

    public static HotelDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            instance = new HotelDAO();
            emf = _emf;
        }
        return instance;
    }

    @Override
    public List<HotelDTO> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Hotel> query = em.createQuery("SELECT h FROM Hotel h", Hotel.class);
            return HotelDTO.toDTOList(query.getResultList());
        }
    }

    @Override
    public HotelDTO getById(int id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Hotel hotel = em.find(Hotel.class, id);
            if(hotel != null){
                return new HotelDTO(hotel);
            }
        }
        return null;
    }

    @Override
    public List<HotelDTO> createFromList(HotelDTO[] hotelDTOS) {
        List<HotelDTO> hotelDTOList = new ArrayList<>();
        for (int index = 0; index < hotelDTOS.length ; index++) {
            HotelDTO newHotelDTO = create(hotelDTOS[index]);
            hotelDTOList.add(newHotelDTO);
        }
        return hotelDTOList;
    }

    @Override
    public HotelDTO create(HotelDTO hotelDTO) {
        Hotel hotel = new Hotel(hotelDTO);
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(hotel);
            em.getTransaction().commit();
        }
        return new HotelDTO(hotel);
    }

    @Override
    public HotelDTO update(int id, HotelDTO hotelDTO) {
        try(EntityManager em = emf.createEntityManager()){
            Hotel hotel = em.find(Hotel.class, id);
            if(hotel != null){
                em.getTransaction().begin();
                hotel.setName(hotelDTO.getName());
                hotel.setAddress(hotelDTO.getAddress());
                hotel.setRooms(hotelDTO.getRooms().stream().map(Room::new).toList());
                em.getTransaction().commit();
            }
            return new HotelDTO(hotel);
        }
    }

    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Hotel hotel = em.find(Hotel.class, id);
            if (hotel != null) {
                em.remove(hotel);
            }
            em.getTransaction().commit();
        }
    }
    public List<RoomDTO> getRoomsForHotel(int hotelId) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Room> query = em.createQuery("SELECT r FROM Room r WHERE r.hotelId = :hotelId", Room.class);
            query.setParameter("hotelId", hotelId);
            return RoomDTO.toDTOList(query.getResultList());
        }
    }
}

