package dat.config;

import dat.controllers.PlaylistController;
import dat.controllers.ProfileController;
import dat.controllers.SongController;
import dat.routes.MuzzPlayerRoutes;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
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

        // Initialize your controllers and routes
        MuzzPlayerRoutes muzzPlayerRoutes = new MuzzPlayerRoutes(
                new ProfileController(emf),
                new PlaylistController(emf),
                new SongController(emf)
        );
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
