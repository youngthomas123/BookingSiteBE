package fontys.s3.individual.bookingsite.business.UserUseCase;

import fontys.s3.individual.bookingsite.domain.model.User;

import java.util.Optional;

public interface GetUserByIdUseCase {

    Optional<User> getUserById(long UserId);

}
