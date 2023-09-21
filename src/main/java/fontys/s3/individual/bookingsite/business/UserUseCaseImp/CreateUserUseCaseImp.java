package fontys.s3.individual.bookingsite.business.UserUseCaseImp;

import fontys.s3.individual.bookingsite.business.UserUseCase.CreateUserUseCase;
import fontys.s3.individual.bookingsite.business.exception.UserNameAlreadyExistsException;
import fontys.s3.individual.bookingsite.domain.request.UserRequest.CreateUserRequest;
import fontys.s3.individual.bookingsite.domain.response.UserResponse.CreateUserResponse;
import fontys.s3.individual.bookingsite.persistence.UserRepository;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.Console;


@Service
@AllArgsConstructor
public class CreateUserUseCaseImp implements CreateUserUseCase
{

    private final UserRepository userRepository;

    @Override
    public CreateUserResponse createUser(CreateUserRequest request)
    {

        if (userRepository.existsByUserName(request.getUsername())) {
            throw new UserNameAlreadyExistsException();
        }

        UserEntity savedUser = saveNewUser(request);

        return CreateUserResponse.builder()
                .id(savedUser.getId())
                .build();


    }
    private UserEntity saveNewUser(CreateUserRequest request) {

        UserEntity newUser = UserEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .build();
        return userRepository.save(newUser);
    }
}
