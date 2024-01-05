package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.InvalidDatesException;
import fontys.s3.individual.bookingsite.business.exception.ItemNotFoundException;
import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.useCase.GetPropertyByIdUseCase;
import fontys.s3.individual.bookingsite.business.util.DateValidator;
import fontys.s3.individual.bookingsite.domain.dto.PropertyPageDTO;
import fontys.s3.individual.bookingsite.domain.response.GetPropertyByIdResponse;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyPictureEntity;
import fontys.s3.individual.bookingsite.persistence.repository.BookingRepository;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyPictureRepository;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetPropertyByIdUseCaseImp implements GetPropertyByIdUseCase
{
    private PropertyRepository propertyRepository;
    private PropertyPictureRepository propertyPictureRepository;
    private BookingRepository bookingRepository;
    private DateValidator dateValidator;

    @Override
    public GetPropertyByIdResponse getPropertyById(long id, String checkin, String checkout)
    {
        //get the propertyEntity and PropertyPictureEntity
        //build the dto
        //build and return response

        if(dateValidator.areDatesValid(checkin, checkout))
        {
            Optional<PropertyEntity> propertyEntity = propertyRepository.findById(id);

            if(propertyEntity.isPresent())
            {
                PropertyEntity property = propertyEntity.get();
                if(property.isEnlisted())
                {
                    List<PropertyPictureEntity> propertyPictureEntities = propertyPictureRepository.findByPropertyEntityId(property.getId());

                    List<String> otherPictures = propertyPictureEntities.stream()
                            .map(PropertyPictureEntity::getPhoto)
                            .toList();

                    LocalDate checkIn = dateValidator.convertToLocalDateObj(checkin);
                    LocalDate checkOut = dateValidator.convertToLocalDateObj(checkout);
                    boolean hasConflictingBookings = bookingRepository.hasConflictingBookings(property.getId(), checkIn, checkOut);


                    PropertyPageDTO propertyPageDTO = PropertyPageDTO.builder()
                            .propertyId(property.getId())
                            .name(property.getName())
                            .description(property.getDescription())
                            .location(property.getLocation())
                            .pricePerNight(property.getPricePerNight())
                            .mainPhoto(property.getMainPhoto())
                            .otherPhotos(otherPictures)
                            .hasConflictingBookings(hasConflictingBookings)
                            .build();

                    GetPropertyByIdResponse response = GetPropertyByIdResponse.builder()
                            .propertyPageDTO(propertyPageDTO)
                            .build();

                    return response;

                }
                else
                {
                    throw new UnauthorizedDataAccessException("This property has been delisted or removed");
                }

            }
            else
            {
                throw new ItemNotFoundException("Property not found");
            }
        }
        else
        {
            throw new InvalidDatesException();
        }



    }
}
