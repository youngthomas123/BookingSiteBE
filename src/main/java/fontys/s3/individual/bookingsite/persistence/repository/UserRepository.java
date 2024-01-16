package fontys.s3.individual.bookingsite.persistence.repository;

import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    boolean existsByUsername(String username);

    UserEntity findByUsername(String username);




    @Query("SELECT u.profilePic FROM UserEntity u WHERE u.id = :id")
    String findProfilePicById(@Param("id") long id);





}
