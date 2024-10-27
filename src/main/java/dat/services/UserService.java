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
        List<UserDTO> users = userDAO.getAll();
        if (users.isEmpty()) {
            throw new IllegalStateException("{ status : 404, 'msg': 'No content found' }"); // e1
        }
        return users;
    }

    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
            throw new IllegalArgumentException("{ status : 400, 'msg': 'Invalid input: Username is required' }"); // e2
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("{ status : 400, 'msg': 'Invalid input: Password is required' }"); // e2
        }
        return userDAO.create(userDTO);
    }

    public double calculateCompatibility(String userOne, String userTwo) {
        double compatibility = userDAO.calculateCompatibility(userOne, userTwo);
        if (compatibility == 0.0) {
            throw new IllegalStateException("{ status : 404, 'msg': 'No shared songs found between users' }"); // e1
        }
        return compatibility;
    }
}
