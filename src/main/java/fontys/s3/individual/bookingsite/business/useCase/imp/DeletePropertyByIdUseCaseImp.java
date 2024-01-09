package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.BookingConflictException;
import fontys.s3.individual.bookingsite.business.exception.ItemNotFoundException;
import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.exception.UserNotFoundException;
import fontys.s3.individual.bookingsite.business.useCase.DeletePropertyByIdUseCase;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.BookingRepository;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyPictureRepository;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DeletePropertyByIdUseCaseImp implements DeletePropertyByIdUseCase
{
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private PropertyRepository propertyRepository;
    private PropertyPictureRepository propertyPictureRepository;
    private AccessToken accessToken;

    @Override
    @Transactional
    public void deletePropertyById(long propertyId)
    {
        //check if user is landlord and property belongs to him

        if(accessToken.hasRole("landlord"))
        {
            Optional<UserEntity> userEntity =  userRepository.findById(accessToken.getUserId());
            if(userEntity.isPresent())
            {
                UserEntity landlord = userEntity.get();
                Optional<PropertyEntity> propertyEntity = propertyRepository.findByIdAndUserEntity(propertyId,landlord);
                if(propertyEntity.isPresent())
                {
                    PropertyEntity property = propertyEntity.get();

                    if(!bookingRepository.existsBookingsAfterTodayForProperty(propertyId, LocalDate.now()) && property.isEnlisted()==false)
                    {
                        bookingRepository.deleteAllByPropertyEntityId(propertyId);
                        propertyPictureRepository.deleteAllByPropertyEntityId(propertyId);
                        propertyRepository.deleteById(propertyId);

                    }
                    else
                    {
                        throw new BookingConflictException("Property has outstanding bookings or is not delisted");
                    }


                }
                else
                {
                    throw new ItemNotFoundException("Property not found or you are not the owner");
                }

            }
            else
            {
                throw new UserNotFoundException("User not found in DB");
            }

        }
        else
        {
            throw new UnauthorizedDataAccessException("Invalid role");
        }
    }
}
