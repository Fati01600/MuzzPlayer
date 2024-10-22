package dat.daos;

import dat.dtos.ProfileDTO;
import dat.entities.Profile;
import dat.entities.Playlist;
import dat.entities.Song;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ProfileDAO {

   private static ProfileDAO instance;
   private static EntityManagerFactory emf;

   private ProfileDAO(EntityManagerFactory _emf) {
      emf = _emf;
   }

   public static ProfileDAO getInstance(EntityManagerFactory _emf) {
      if (instance == null) {
         instance = new ProfileDAO(_emf);
      }
      return instance;
   }

   // Henter alle profiler som ProfileDTO'er
   public List<ProfileDTO> getAll() {
      try (EntityManager em = emf.createEntityManager()) {
         TypedQuery<Profile> query = em.createQuery("SELECT p FROM Profile p", Profile.class);
         return ProfileDTO.toDTOList(query.getResultList());
      }
   }

   // Finder en profil baseret på ID og returnerer en ProfileDTO
   public ProfileDTO getById(int id) {
      try (EntityManager em = emf.createEntityManager()) {
         Profile profile = em.find(Profile.class, id);
         if (profile != null) {
            return new ProfileDTO(profile);
         }
      }
      return null;
   }

   // Opretter en ny profil baseret på ProfileDTO
   public ProfileDTO create(ProfileDTO profileDTO) {
      Profile profile = new Profile(profileDTO);
      try (EntityManager em = emf.createEntityManager()) {
         em.getTransaction().begin();
         em.persist(profile);
         em.getTransaction().commit();
      }
      return new ProfileDTO(profile);
   }

   // Opdaterer en eksisterende profil baseret på deres ID
   public ProfileDTO update(int id, ProfileDTO profileDTO) {
      try (EntityManager em = emf.createEntityManager()) {
         Profile profile = em.find(Profile.class, id);
         if (profile != null) {
            em.getTransaction().begin();
            profile.setName(profileDTO.getName());
            profile.setPlaylists(profileDTO.getPlaylists().stream()
                    .map(Playlist::new)
                    .toList());
            em.getTransaction().commit();
            return new ProfileDTO(profile);
         }
      }
      return null;
   }

   // Sletter en profil baseret på deres ID
   public void delete(int id) {
      try (EntityManager em = emf.createEntityManager()) {
         em.getTransaction().begin();
         Profile profile = em.find(Profile.class, id);
         if (profile != null) {
            em.remove(profile);
         }
         em.getTransaction().commit();
      }
   }

   //metode til at beregne kompatibilitet mellem to profiler
   public double calculateCompatibility(int id1, int id2) {
      try (EntityManager em = emf.createEntityManager()) {
         Profile profile1 = em.find(Profile.class, id1);
         Profile profile2 = em.find(Profile.class, id2);

         if (profile1 == null || profile2 == null) {
            return -1; // Return -1 hvis en eller begge profiler ikke findes
         }

         List<Song> songsProfile1 = profile1.getPlaylists().stream()
                 .flatMap(playlist -> playlist.getSongs().stream())
                 .toList();

         List<Song> songsProfile2 = profile2.getPlaylists().stream()
                 .flatMap(playlist -> playlist.getSongs().stream())
                 .toList();

         // Beregner antallet af fælles sange
         long sharedSongs = songsProfile1.stream()
                 .filter(songsProfile2::contains)
                 .count();

            // Beregner det samlede antal unikke sange
         int totalUniqueSongs = (int) (songsProfile1.size() + songsProfile2.size() - sharedSongs);
         return totalUniqueSongs > 0 ? (double) sharedSongs / totalUniqueSongs * 100 : 0;
      }
   }
}
