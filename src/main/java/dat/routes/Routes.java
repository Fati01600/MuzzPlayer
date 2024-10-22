package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private HotelRoutes hotelRoutes = new HotelRoutes();
    private RoomRoutes roomRoutes = new RoomRoutes();

    public EndpointGroup getRoutes (){
        return () -> {
            //get("/", ctx -> ctx.result("Hello World"));
            path("/hotels", hotelRoutes.getRoutes());
            path("/room", roomRoutes.getRoutes());
        };
    }
}
