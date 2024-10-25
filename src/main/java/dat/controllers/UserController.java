package dat.controllers;

import dat.dtos.UserDTO;
import dat.services.UserService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void getAllUsers(Context ctx) {
        List<UserDTO> users = userService.getAllUsers();
        if (users != null) {
            ctx.json(users);
        } else {
            ctx.status(404);
        }
    }

    public void getUserById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            UserDTO user = userService.getUserById(id);
            if (user != null) {
                ctx.json(user);
            } else {
                ctx.status(404).result("User not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid user ID format");
        }
    }

    public void createUser(Context ctx) {
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
        UserDTO newUserDTO = userService.createUser(userDTO);
        ctx.status(HttpStatus.CREATED).json(newUserDTO);
    }

    public void updateUser(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);
        UserDTO updatedUserDTO = userService.updateUser(id, userDTO);
        if (updatedUserDTO != null) {
            ctx.status(HttpStatus.OK).json(updatedUserDTO);
        } else {
            ctx.status(404).result("User not found");
        }
    }

    public void deleteUser(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        userService.deleteUser(id);
        ctx.result("Deleted user with ID: " + id);
    }

    public void getCompatibility(Context ctx) {
        int id1 = Integer.parseInt(ctx.pathParam("id1"));
        int id2 = Integer.parseInt(ctx.pathParam("id2"));
        double compatibility = userService.calculateCompatibility(id1, id2);
        if (compatibility >= 0) {
            ctx.json(Collections.singletonMap("compatibility", compatibility));
        } else {
            ctx.status(404).result("One or both users not found");
        }
    }
}