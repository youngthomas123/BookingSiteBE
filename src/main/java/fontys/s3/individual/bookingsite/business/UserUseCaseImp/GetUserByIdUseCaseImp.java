package fontys.s3.individual.bookingsite.business.UserUseCaseImp;

import fontys.s3.individual.bookingsite.business.UserConverter;
import fontys.s3.individual.bookingsite.business.UserUseCase.GetUserByIdUseCase;
import fontys.s3.individual.bookingsite.domain.model.User;
import fontys.s3.individual.bookingsite.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetUserByIdUseCaseImp implements GetUserByIdUseCase
{

    private UserRepository userRepository;


    @Override
    public Optional<User> getUserById(long UserId) {
        return userRepository.findById(UserId).map(UserConverter::convert);
    }
}
