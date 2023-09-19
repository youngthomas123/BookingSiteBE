package fontys.s3.individual.bookingsite.business.UserUseCaseImp;

import fontys.s3.individual.bookingsite.business.UserConverter;
import fontys.s3.individual.bookingsite.business.UserUseCase.GetAllUsersUseCase;
import fontys.s3.individual.bookingsite.domain.model.User;
import fontys.s3.individual.bookingsite.domain.response.UserResponse.GetAllUsersResponse;
import fontys.s3.individual.bookingsite.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllUsersUseCaseImp implements GetAllUsersUseCase
{
    private UserRepository userRepository;

    @Override
    public GetAllUsersResponse getAllUsers()
    {
        List<User> users = userRepository.findAll()
                .stream()
                .map(UserConverter::convert)
                .toList();

        return GetAllUsersResponse.builder()
                .users(users)
                .build();
    }
}
