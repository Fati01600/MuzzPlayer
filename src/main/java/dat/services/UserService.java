package dat.services;

import dat.daos.UserDAO;
import dat.dtos.UserDTO;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService(EntityManagerFactory emf) {
        this.userDAO = UserDAO.getInstance(emf);
    }
    public void deleteUser(String username) {
        userDAO.delete(username);
    }

    public double calculateCompatibility(String userOne, String userTwo) {
        return userDAO.calculateCompatibility(userOne, userTwo);
    }
}