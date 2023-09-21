package fontys.s3.individual.bookingsite.business;

import fontys.s3.individual.bookingsite.domain.model.User;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
// testing
public class UserConverter
{
    private UserConverter(){}

    public static User convert(UserEntity user) {
        return User.builder()
                .id(user.getId())
                .password(user.getPassword())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }


}
