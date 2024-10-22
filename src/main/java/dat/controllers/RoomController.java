package dat.controllers;

import dat.config.HibernateConfig;
import dat.daos.RoomDAO;
import dat.dtos.RoomDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RoomController {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("hotel");
    RoomDAO roomDAO = RoomDAO.getInstance(emf);

    // Create a logger instance for this class
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
    private static final Logger debugLogger = LoggerFactory.getLogger("app");

    public RoomController (){
    }

    public void getRooms(Context ctx){
        List<RoomDTO> roomDTOS = roomDAO.getAll();
        if (roomDTOS != null) {
            ctx.json(roomDTOS);
        } else {
            ctx.status(404);
        }
    }

    public void getById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        RoomDTO roomDTO = roomDAO.getById(id);
        if (roomDTO != null) {
            ctx.json(roomDTO);
        } else {
            ctx.status(404);
        }
    }

    public void createRooms (Context ctx){
        try {
            RoomDTO[] roomDTOS = ctx.bodyAsClass(RoomDTO[].class);
            List<RoomDTO> newRoomDTOs = roomDAO.createFromList(roomDTOS);
            ctx.status(HttpStatus.CREATED);
            ctx.json(newRoomDTOs);
            } catch (IllegalStateException e){
            throw new IllegalStateException("400 Bad Request. Invalid JSON for room");
        }
    }

    public void createRoom (Context ctx){
        RoomDTO roomDTO = ctx.bodyAsClass(RoomDTO.class);
        RoomDTO newRoomDTO = roomDAO.create(roomDTO);
        ctx.status(HttpStatus.CREATED);
        ctx.json(newRoomDTO);
    }

    public void update (Context ctx){
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            RoomDTO roomDTO = ctx.bodyAsClass(RoomDTO.class);
            roomDTO = roomDAO.update(id, roomDTO);
            ctx.status(HttpStatus.OK);
            ctx.json(roomDTO);
        } catch (IllegalStateException e){
            throw new IllegalStateException("400 Bad Request. Invalid JSON for updating room");
        }
    }

    public void delete (Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        roomDAO.delete(id);
        ctx.result("deleted: " + id);
    }
}
