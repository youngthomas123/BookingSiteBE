package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.UserNameAlreadyExistsException;
import fontys.s3.individual.bookingsite.business.useCase.CreateUserUseCase;
import fontys.s3.individual.bookingsite.domain.request.CreateUserRequest;
import fontys.s3.individual.bookingsite.domain.response.CreateUserResponse;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@AllArgsConstructor
public class CreateUserUseCaseImp implements CreateUserUseCase
{
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CreateUserResponse createUser(CreateUserRequest request)
    {
        boolean doesUsernameAlreadyExist = userRepository.existsByUsername(request.getUsername());
        if(!doesUsernameAlreadyExist)
        {

            String encodedPassword = passwordEncoder.encode(request.getPassword());
            UserEntity user = UserEntity.builder()
                    .username(request.getUsername())
                    .password(encodedPassword)
                    .type(request.getType())
                    .build();



             UserEntity savedUser=userRepository.save(user);



            CreateUserResponse response = CreateUserResponse.builder()
                    .id(savedUser.getId())
                    .build();


            return response;
        }
        else
        {
             throw new UserNameAlreadyExistsException();
        }
    }
}
