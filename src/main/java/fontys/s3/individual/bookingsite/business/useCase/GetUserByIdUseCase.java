package fontys.s3.individual.bookingsite.business.useCase;

import fontys.s3.individual.bookingsite.domain.dto.UserDetailsDTO;
import fontys.s3.individual.bookingsite.domain.dto.UserSignUpDTO;

import java.util.Optional;

public interface GetUserByIdUseCase {

    Optional<UserDetailsDTO> getUserById(long UserId);

}
