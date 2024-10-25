package dat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.controllers.PlaylistController;
import dat.controllers.UserController;
import dat.controllers.SongController;
import dat.security.controllers.AccessController;
import dat.security.controllers.SecurityController;
import dat.services.PlaylistService;
import dat.services.UserService;
import dat.services.SongService;
import dat.routes.MuzzPlayerRoutes;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfig {

    private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("muzzplayer");

    public static void configuration(JavalinConfig config) {
        config.showJavalinBanner = false; // Disable the default Javalin banner
        config.bundledPlugins.enableRouteOverview("/routes");
        config.router.contextPath = "/api/v1"; // Set base path for all endpoints

        // Initialize services
        UserService userService = new UserService(emf);
        PlaylistService playlistService = new PlaylistService(emf);
        SongService songService = new SongService(emf);

        // Initialize controllers with services
        UserController userController = new UserController(userService);
        PlaylistController playlistController = new PlaylistController(playlistService, userService);
        SongController songController = new SongController(songService);

        // Initialize routes
        MuzzPlayerRoutes muzzPlayerRoutes = new MuzzPlayerRoutes(userController, playlistController, songController);

        // Register routes
        config.router.apiBuilder(muzzPlayerRoutes.getRoutes());
    }

    public static Javalin startServer(int port) {
        // Create the Javalin instance
        Javalin app = Javalin.create(ApplicationConfig::configuration);

        // Exception handling (optional)
        app.exception(Exception.class, (e, ctx) -> {
            logger.error("An error occurred: {}", e.getMessage(), e);
            ctx.status(500).result("An internal error occurred.");
        });

        // Start the server
        app.start(port);
        return app;
    }

    public static void stopServer(Javalin app) {
        app.stop();
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
