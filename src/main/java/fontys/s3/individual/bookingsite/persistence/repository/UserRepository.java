package fontys.s3.individual.bookingsite.persistence.repository;

import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    boolean existsByUsername(String username);

    UserEntity findByUsername(String username);

    @Query(value = "select profile_pic " +
            "from users " + // Added space after 'users'
            "where id = :id", nativeQuery = true)
    String findProfilePicById(long id);




}
