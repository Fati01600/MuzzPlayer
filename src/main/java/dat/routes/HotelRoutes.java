package dat.routes;

import dat.controllers.HotelController;
import dat.security.controllers.SecurityController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class HotelRoutes {

    HotelController hotelController = new HotelController();
    SecurityController securityController = SecurityController.getInstance();

    public EndpointGroup getRoutes() {
        return () -> {
            post("/", hotelController::createHotels, Role.ADMIN);
            //post("/", hotelController::createHotel); kan man ikke have flere post request i samme endpoint?
            get("/", hotelController::getHotels, Role.ANYONE);
            get("/{id}", hotelController::getById, Role.ANYONE);
            get("/{id}/rooms", hotelController::getRoomsForHotel, Role.ANYONE);
            put("/{id}", hotelController::update, Role.USER);
            delete("/{id}", hotelController::delete, Role.USER);
        };
    }
}