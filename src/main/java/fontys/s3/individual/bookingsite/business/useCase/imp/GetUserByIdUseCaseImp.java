package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.useCase.GetUserByIdUseCase;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.dto.UserDetailsDTO;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetUserByIdUseCaseImp implements GetUserByIdUseCase {
    private UserRepository userRepository;
    private AccessToken requestAccessToken;


    @Override
    public Optional<UserDetailsDTO> getUserById(long userId) //all roles should be able to access
    {
        if (requestAccessToken.getUserId() == userId)
        {
            Optional<UserEntity> userEntity = userRepository.findById(userId);

            if (userEntity.isPresent())
            {
                UserEntity user = userEntity.get();  // Get the actual UserEntity object

                UserDetailsDTO userDetailsDTO = UserDetailsDTO.builder()
                        .username(user.getUsername())
                        .dateCreated(user.getDateCreated())
                        .type(user.getType())
                        .email(user.getEmail())
                        .bio(user.getBio())
                        .phoneNumber(user.getPhoneNumber())
                        .build();

                return Optional.of(userDetailsDTO);

            }
            else
            {
                return Optional.empty(); // User not found, return an empty Optional
            }
        }
        else
        {
            throw new UnauthorizedDataAccessException("User id not equal to route parameter id");
        }


    }


}
