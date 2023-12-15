package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.exception.UserNotFoundException;
import fontys.s3.individual.bookingsite.business.useCase.UpdateUserByIdUseCase;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.request.UpdateUserRequest;
import fontys.s3.individual.bookingsite.domain.response.UpdateUserResponse;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateUserByIdImp implements UpdateUserByIdUseCase
{
    private UserRepository userRepository;
    private AccessToken requestAccessToken;

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest request, long id)
    {
        if(requestAccessToken.getUserId() == id)
        {
            Optional<UserEntity> userEntity = userRepository.findById(id);

            if(userEntity.isPresent())
            {
                UserEntity user = userEntity.get();

                user.setPhoneNumber(request.getPhoneNumber());
                user.setEmail(request.getEmail());
                user.setBio(request.getBio());

                UserEntity savedUser = userRepository.save(user); //update the already existing entity
                UpdateUserResponse response = UpdateUserResponse.builder()
                        .id(savedUser.getId())
                        .build();
                return response;
            }
            else
            {
                throw new UserNotFoundException("User id not found");
            }
        }
        else
        {
            throw new UnauthorizedDataAccessException("User id not equal to route parameter id");
        }

    }
}
