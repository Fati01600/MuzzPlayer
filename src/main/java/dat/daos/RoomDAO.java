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

public class RoomDAO extends DAO<RoomDTO> {

    private static RoomDAO instance;
    private static EntityManagerFactory emf;

    private RoomDAO() {
        // private constructor
    }

    public static RoomDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            instance = new RoomDAO();
            emf = _emf;
        }
        return instance;
    }

    @Override
    public List<RoomDTO> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Room> query = em.createQuery("SELECT r FROM Room r", Room.class);
            return RoomDTO.toDTOList(query.getResultList());
        }
    }

    @Override
    public RoomDTO getById(int id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Room room = em.find(Room.class, id);
            if(room != null){
                return new RoomDTO(room);
            }
        }
        return null;
    }

    @Override
    public List<RoomDTO> createFromList(RoomDTO[] roomDTOS) {
        List<RoomDTO> roomDTOList = new ArrayList<>();
        for (int index = 0; index < roomDTOS.length ; index++) {
            RoomDTO newRoomDTO = create(roomDTOS[index]);
            roomDTOList.add(newRoomDTO);
        }
        return roomDTOList;
    }

    @Override
    public RoomDTO create(RoomDTO roomDTO) {
        Room room = new Room(roomDTO);
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(room);
            em.getTransaction().commit();
        }
        return new RoomDTO(room);
    }

    @Override
    public RoomDTO update(int id, RoomDTO roomDTO) {
        try(EntityManager em = emf.createEntityManager()){
            Room room = em.find(Room.class, id);
            if(room != null){
                em.getTransaction().begin();
                room.setHotelId(roomDTO.getHotelId());
                room.setNumber(roomDTO.getNumber());
                room.setPrice(roomDTO.getPrice());
                em.getTransaction().commit();
            }
            return new RoomDTO(room);
        }
    }

    @Override
    public void delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Room room = em.find(Room.class, id);
            if (room != null) {
                em.remove(room);
            }
            em.getTransaction().commit();
        }
    }
}
