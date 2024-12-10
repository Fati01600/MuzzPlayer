package dat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.controllers.PlaylistController;
import dat.controllers.UserController;
import dat.controllers.SongController;
import dat.security.controllers.AccessController;
import dat.security.controllers.SecurityController;
import dat.security.exceptions.ApiException;
import dat.security.routes.SecurityRoutes;
import dat.services.PlaylistService;
import dat.services.UserService;
import dat.services.SongService;
import dat.routes.MuzzPlayerRoutes;
import dat.utils.Utils;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfig {

    private static Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private static ObjectMapper jsonMapper = new Utils().getObjectMapper();
    private static SecurityController securityController = SecurityController.getInstance();
    private static AccessController accessController = new AccessController();


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
        PlaylistController playlistController = new PlaylistController(playlistService);
        SongController songController = new SongController(songService);

        // Initialize routes
        MuzzPlayerRoutes routes = new MuzzPlayerRoutes(userController, playlistController, songController);

        // Register routes
        config.router.apiBuilder(routes.getRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecuredRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecurityRoutes());    }

    public static Javalin startServer(int port) {
        Javalin app = Javalin.create(ApplicationConfig::configuration);
        app.beforeMatched(accessController::accessHandler);
        app.exception(Exception.class, ApplicationConfig::generalExceptionHandler);
        app.exception(ApiException.class, ApplicationConfig::apiExceptionHandler);
        app.before(ApplicationConfig::corsHeaders);
        app.options("/*", ApplicationConfig::corsHeadersOptions);
        app.start(port);
        return app;
    }

    public static void stopServer(Javalin app) {
        app.stop();
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    private static void generalExceptionHandler(Exception e, Context ctx) {
        logger.error("An unhandled exception occurred", e.getMessage());
        ctx.json(Utils.convertToJsonMessage(ctx, "error", e.getMessage()));
    }

    public static void apiExceptionHandler(ApiException e, Context ctx) {
        ctx.status(e.getCode());
        logger.warn("An API exception occurred: Code: {}, Message: {}", e.getCode(), e.getMessage());
        ctx.json(Utils.convertToJsonMessage(ctx, "warning", e.getMessage()));
    }
    private static void corsHeaders(Context ctx) {
        ctx.header("Access-Control-Allow-Origin", "*");
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
    }

    private static void corsHeadersOptions(Context ctx) {
        ctx.header("Access-Control-Allow-Origin", "*");
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.header("Access-Control-Allow-Credentials", "true");
        ctx.status(204);
    }

}
