package dat.daos;

import dat.entities.Playlist;
import dat.dtos.UserDTO;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserDAO {

   private static UserDAO instance;
   private static EntityManagerFactory emf;

   private UserDAO(EntityManagerFactory _emf) {
      emf = _emf;
   }

   public static UserDAO getInstance(EntityManagerFactory _emf) {
      if (instance == null) {
         instance = new UserDAO(_emf);
      }
      return instance;
   }


   public List<UserDTO> getAll() {
      try (EntityManager em = emf.createEntityManager()) {
         TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
         List<User> users = query.getResultList();
         if (users.isEmpty()) {
            throw new IllegalStateException("{ status : 404, 'msg': 'No content found' }"); // e1
         }
         return UserDTO.toDTOList(users);
      }
   }


   public UserDTO getByUserName(String userName) {
      try (EntityManager em = emf.createEntityManager()) {
         User user = em.find(User.class, userName);
         if (user == null) {
            throw new IllegalStateException("{ status : 404, 'msg': 'Resource not found' }"); // e1
         }
         return new UserDTO(user);
      }
   }

   public UserDTO create(UserDTO userDTO) {
      if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
         throw new IllegalArgumentException("{ status : 400, 'msg': 'Invalid input: Username is required' }"); // e2
      }
      if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
         throw new IllegalArgumentException("{ status : 400, 'msg': 'Invalid input: Password is required' }"); // e2
      }

      User user = new User(userDTO);
      try (EntityManager em = emf.createEntityManager()) {
         em.getTransaction().begin();
         em.persist(user);
         em.getTransaction().commit();
      }
      return new UserDTO(user);
   }

   public UserDTO update(String username, UserDTO userDTO) {
      EntityManager em = emf.createEntityManager();


      try {
         em.getTransaction().begin();

         User user = em.find(User.class, username);
         if (user == null) throw new IllegalStateException("{ status : 404, 'msg': 'Resource not found' }");

         // Hash the new password and update the user entity
         String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
         user.setPassword(hashedPassword);

         User updatedUser = em.merge(user);
         em.getTransaction().commit();

         return new UserDTO(updatedUser);
      } catch (Exception e) {
         if (em.getTransaction().isActive()) em.getTransaction().rollback();
         throw new RuntimeException("Update failed: " + e.getMessage(), e);
      } finally {
         em.close();
      }
   }

   public void delete(String userName) {
      try (EntityManager em = emf.createEntityManager()) {
         User user = em.find(User.class, userName);
         if (user == null) {
            throw new IllegalStateException("{ status : 404, 'msg': 'Resource not found' }"); // e1
         }
         em.getTransaction().begin();
         em.remove(user);
         em.getTransaction().commit();
      }
   }

   public double calculateCompatibility(String userOne, String userTwo) {
      EntityManager em = emf.createEntityManager();

      try {
         String query = """
                SELECT COUNT(s.id)
                FROM Playlist p1
                JOIN p1.songs s
                WHERE p1.user.username = :userOne
                AND s IN (SELECT s2 FROM Playlist p2 JOIN p2.songs s2 WHERE p2.user.username = :userTwo)
            """;

         Long sharedSongsCount = em.createQuery(query, Long.class)
                 .setParameter("userOne", userOne)
                 .setParameter("userTwo", userTwo)
                 .getSingleResult();

         String totalSongsQuery = """
                SELECT COUNT(DISTINCT s.id)
                FROM Playlist p
                JOIN p.songs s
                WHERE p.user.username IN (:userOne, :userTwo)
            """;

         Long totalUniqueSongs = em.createQuery(totalSongsQuery, Long.class)
                 .setParameter("userOne", userOne)
                 .setParameter("userTwo", userTwo)
                 .getSingleResult();

         return totalUniqueSongs != null && totalUniqueSongs > 0 ? (double) sharedSongsCount / totalUniqueSongs * 100 : 0;
      } finally {
         em.close();
      }
   }
}
