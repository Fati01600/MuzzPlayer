package dat.controllers;

import dat.services.UserService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;


public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void deleteUser(Context ctx) {
        String username = ctx.pathParam("username");
        userService.deleteUser(username);
        ctx.result("Deleted user with username: " + username);
    }


    public void getCompatibility(Context ctx) {
        String userOne = ctx.pathParam("id1");
        String userTwo = ctx.pathParam("id2");
        double compatibility = userService.calculateCompatibility(userOne, userTwo);
        if (compatibility >= 0) {
            ctx.json(Collections.singletonMap("compatibility", compatibility));
        } else {
            ctx.status(404).result("One or both users not found");
        }

    }
}