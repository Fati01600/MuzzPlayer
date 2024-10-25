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

    public List<UserDTO> getAllUsers() {
        return userDAO.getAll();
    }

    public UserDTO getUserById(int id) {
        return userDAO.getById(id);
    }

    public UserDTO createUser(UserDTO userDTO) {
        return userDAO.create(userDTO);
    }

    public UserDTO updateUser(int id, UserDTO userDTO) {
        return userDAO.update(id, userDTO);
    }

    public void deleteUser(int id) {
        userDAO.delete(id);
    }

    public double calculateCompatibility(String userOne, String userTwo) {
        return userDAO.calculateCompatibility(userOne, userTwo);
    }
}