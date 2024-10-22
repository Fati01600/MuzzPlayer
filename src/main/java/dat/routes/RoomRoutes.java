package dat.routes;

import dat.controllers.HotelController;
import dat.controllers.RoomController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class RoomRoutes {

    RoomController roomController = new RoomController();

    public EndpointGroup getRoutes (){
        return () -> {
            post("/", roomController::createRooms);
            //post("/", roomController::createRoom); kan man ikke have flere post request i samme endpoint?
            get("/", roomController::getRooms);
            get("/{id}", roomController::getById);
            put("/{id}", roomController::update);
            delete("/{id}", roomController::delete);
        };
    }
}
