package fontys.s3.individual.bookingsite.persistence.repository;

import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    boolean existsByUsername(String username);
}
