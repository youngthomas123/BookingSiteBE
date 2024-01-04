package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.BadRequestException;
import fontys.s3.individual.bookingsite.business.exception.ItemNotFoundException;
import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.exception.UserNotFoundException;
import fontys.s3.individual.bookingsite.business.useCase.UpdatePropertyStatusUseCase;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.response.UpdatePropertyStatusResponse;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdatePropertyStatusUseCaseImp implements UpdatePropertyStatusUseCase
{
    private PropertyRepository propertyRepository;
    private UserRepository userRepository;
    private AccessToken requestAccessToken;

    @Override
    public UpdatePropertyStatusResponse updatePropertyStatus(long propertyId, String status)
    {

        //check if user is landlord and he owns the property

        if(requestAccessToken.hasRole("landlord"))
        {
            Optional<UserEntity> userEntity = userRepository.findById(requestAccessToken.getUserId());
            if(userEntity.isPresent())
            {
                UserEntity landlord = userEntity.get();

                Optional<PropertyEntity> propertyEntity = propertyRepository.findByIdAndUserEntity(propertyId, landlord);

                if(propertyEntity.isPresent())
                {
                    PropertyEntity property = propertyEntity.get();
                    if(Objects.equals(status, "enlist"))
                    {
                        property.setEnlisted(true);
                        PropertyEntity savedProperty = propertyRepository.save(property);
                        UpdatePropertyStatusResponse response = UpdatePropertyStatusResponse.builder()
                                .propertyId(savedProperty.getId())
                                .enlisted(savedProperty.isEnlisted())
                                .build();
                        return response;
                    }
                    else if (Objects.equals(status, "delist"))
                    {
                        property.setEnlisted(false);
                        PropertyEntity savedProperty = propertyRepository.save(property);
                        UpdatePropertyStatusResponse response = UpdatePropertyStatusResponse.builder()
                                .propertyId(savedProperty.getId())
                                .enlisted(savedProperty.isEnlisted())
                                .build();
                        return response;
                    }
                    else
                    {
                        throw new BadRequestException("Status values - 'enlist' or 'delist' and not "+status);
                    }


                }
                else
                {
                    throw new ItemNotFoundException("Property does not exist or you are not the owner");
                }

            }
            else
            {
                throw new UserNotFoundException("Could not find user with userId:"+requestAccessToken.getUserId());
            }
        }
        else
        {
            throw new UnauthorizedDataAccessException("Invalid role");
        }


    }
}
