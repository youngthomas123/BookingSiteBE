package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.BadRequestException;
import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.exception.UserNotFoundException;
import fontys.s3.individual.bookingsite.business.useCase.UpdateUserStatusUseCase;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.response.UpdateUserStatusResponse;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateUserStatusUseCaseImp implements UpdateUserStatusUseCase
{
    private UserRepository userRepository;
    private AccessToken requestAccessToken;
    private PropertyRepository propertyRepository;
    @Override
    @Transactional
    public UpdateUserStatusResponse updateUserStatus(long userId, String status)
    {

        //The user should be an admin
        //Find the user that needs to be banned or unbanned.
        // Change the users status
        if(requestAccessToken.hasRole("admin"))
        {
            Optional<UserEntity> userEntity = userRepository.findById(userId);

            if(userEntity.isPresent() && !Objects.equals(userEntity.get().getType(), "admin"))
            {
                UserEntity user = userEntity.get();
                if(Objects.equals(status, "ban"))
                {
                    user.setBanned(true);
                    UserEntity savedUser = userRepository.save(user);
                    if(Objects.equals(user.getType(), "landlord"))
                    {
                        propertyRepository.setAllPropertiesNotEnlistedForUser(user);
                    }
                    UpdateUserStatusResponse response = UpdateUserStatusResponse.builder()
                            .userId(savedUser.getId())
                            .isBanned(savedUser.isBanned())
                            .build();
                    return response;
                }

                else if (Objects.equals(status, "unban"))
                {
                    user.setBanned(false);
                    UserEntity savedUser = userRepository.save(user);
                    UpdateUserStatusResponse response = UpdateUserStatusResponse.builder()
                            .userId(savedUser.getId())
                            .isBanned(savedUser.isBanned())
                            .build();
                    return response;
                }
                else
                {
                    throw new BadRequestException("Status values - 'ban' or 'unban' and not "+status);
                }
            }
            else
            {
                throw new UserNotFoundException("The user that was supposed to be banned or unbanned was not found in the DB");
            }

        }
        else
        {
            throw new UnauthorizedDataAccessException("Invalid role");
        }



    }
}
