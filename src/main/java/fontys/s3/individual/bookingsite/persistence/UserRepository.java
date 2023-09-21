package fontys.s3.individual.bookingsite.persistence;

import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository
{
    List<UserEntity> findAll();

    Optional<UserEntity> findById(long UserId);
    boolean existsById(long id);

    boolean existsByUserName(String username);


    UserEntity save(UserEntity user);




}
