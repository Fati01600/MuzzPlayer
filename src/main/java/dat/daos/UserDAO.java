package dat.daos;


import dat.entities.Playlist;
import dat.dtos.UserDTO;
import dat.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

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


   public UserDTO create(UserDTO userDTO) {
      User user = new User(userDTO);
      try (EntityManager em = emf.createEntityManager()) {
         em.getTransaction().begin();
         em.persist(user);
         em.getTransaction().commit();
      }
      return new UserDTO(user);
   }


   public UserDTO update(int id, UserDTO userDTO) {
      try (EntityManager em = emf.createEntityManager()) {
         User user = em.find(User.class, id);
         if (user != null) {
            em.getTransaction().begin();
            user.setUsername(userDTO.getUsername());
            user.setPlaylists(userDTO.getPlaylists().stream()
                    .map(Playlist::new)
                    .toList());
            em.getTransaction().commit();
            return new UserDTO(user);
         }
      }
      return null;
   }


   public void delete(int id) {
      try (EntityManager em = emf.createEntityManager()) {
         em.getTransaction().begin();
         User user = em.find(User.class, id);
         if (user != null) {
            em.remove(user);
         }
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