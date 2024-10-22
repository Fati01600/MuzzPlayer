package dat.controllers;

import dat.config.HibernateConfig;
import dat.daos.HotelDAO;
import dat.dtos.HotelDTO;
import dat.dtos.RoomDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HotelController {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("hotel");
    HotelDAO hotelDAO = HotelDAO.getInstance(emf);

    // Create a logger instance for this class
    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);
    private static final Logger debugLogger = LoggerFactory.getLogger("app");

    public HotelController (){}

    public void getHotels(Context ctx){
        List<HotelDTO> hotelDTOS = hotelDAO.getAll();
        if (hotelDTOS != null) {
            ctx.json(hotelDTOS);
        } else {
            ctx.status(404);
        }
    }

    public void getById(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        HotelDTO hotelDTO = hotelDAO.getById(id);
        if (hotelDTO != null) {
            ctx.json(hotelDTO);
        } else {
            ctx.status(404);
        }
    }

    public void getRoomsForHotel(Context ctx) {
        int hotelId = Integer.parseInt(ctx.pathParam("id"));
        List<RoomDTO> rooms = hotelDAO.getRoomsForHotel(hotelId);
        if (rooms != null && !rooms.isEmpty()) {
            ctx.json(rooms);
        } else {
            ctx.status(404).result("No rooms found for hotel with ID: " + hotelId);
        }
    }

    public void createHotels(Context ctx){
        try {
            HotelDTO[] hotelDTOS = ctx.bodyAsClass(HotelDTO[].class);
            for (HotelDTO hotelDTO : hotelDTOS) {
                if (hotelDTO.getRooms() == null) {
                    hotelDTO.setRooms(new ArrayList<>());  // Sørg for tom liste, hvis der ikke er værelser
                }
            }
            List<HotelDTO> newHotelDTOs = hotelDAO.createFromList(hotelDTOS);
            ctx.status(HttpStatus.CREATED);
            ctx.json(newHotelDTOs);
        } catch (IllegalStateException e){
            throw new IllegalStateException("400 Bad Request. Invalid JSON for hotel");
        }
    }

    public void createHotel(Context ctx) {
        HotelDTO hotelDTO = ctx.bodyAsClass(HotelDTO.class);
        if (hotelDTO.getRooms() == null) {
            hotelDTO.setRooms(new ArrayList<>());  // Initialiser tom liste, hvis der ikke er værelser
        }
        HotelDTO newHotelDTO = hotelDAO.create(hotelDTO);
        ctx.status(HttpStatus.CREATED);
        ctx.json(newHotelDTO);
    }

    public void update (Context ctx){
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            HotelDTO hotelDTO = ctx.bodyAsClass(HotelDTO.class);
            hotelDTO = hotelDAO.update(id, hotelDTO);
            ctx.status(HttpStatus.OK);
            ctx.json(hotelDTO);
        } catch (IllegalStateException e){
            throw new IllegalStateException("400 Bad Request. Invalid JSON for updating hotel");
        }
    }

    public void delete (Context ctx){
        int id = Integer.parseInt(ctx.pathParam("id"));
        hotelDAO.delete(id);
        ctx.result("deleted: " + id);
    }
}
