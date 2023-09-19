package fontys.s3.individual.bookingsite.persistence;

import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;

import java.util.List;

public interface UserRepository
{
    List<UserEntity> findAll();

}
