package dat.controllers;

import dat.daos.ProfileDAO;
import dat.dtos.ProfileDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class ProfileController {

    private final EntityManagerFactory emf;
    ProfileDAO profileDAO;

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    public ProfileController(EntityManagerFactory emf) {
        this.emf = emf;
        this.profileDAO = ProfileDAO.getInstance(emf);
    }

    public void getAllProfiles(Context ctx) {
        List<ProfileDTO> profiles = profileDAO.getAll();
        if (profiles != null) {
            ctx.json(profiles);
        } else {
            ctx.status(404);
        }
    }

    public void getProfileById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        ProfileDTO profile = profileDAO.getById(id);
        if (profile != null) {
            ctx.json(profile);
        } else {
            ctx.status(404).result("Profile not found");
        }
    }

    public void createProfile(Context ctx) {
        ProfileDTO profileDTO = ctx.bodyAsClass(ProfileDTO.class);
        ProfileDTO newProfileDTO = profileDAO.create(profileDTO);
        ctx.status(HttpStatus.CREATED).json(newProfileDTO);
    }

    public void updateProfile(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        ProfileDTO profileDTO = ctx.bodyAsClass(ProfileDTO.class);
        ProfileDTO updatedProfileDTO = profileDAO.update(id, profileDTO);
        if (updatedProfileDTO != null) {
            ctx.status(HttpStatus.OK).json(updatedProfileDTO);
        } else {
            ctx.status(404).result("Profile not found");
        }
    }

    public void deleteProfile(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        profileDAO.delete(id);
        ctx.result("Deleted profile with ID: " + id);
    }

    public void getCompatibility(Context ctx) {
        int id1 = Integer.parseInt(ctx.pathParam("id1"));
        int id2 = Integer.parseInt(ctx.pathParam("id2"));

        double compatibility = profileDAO.calculateCompatibility(id1, id2);
        if (compatibility >= 0) {
            ctx.json(Collections.singletonMap("compatibility", compatibility));
        } else {
            ctx.status(404).result("One or both profiles not found");
        }
    }

}
