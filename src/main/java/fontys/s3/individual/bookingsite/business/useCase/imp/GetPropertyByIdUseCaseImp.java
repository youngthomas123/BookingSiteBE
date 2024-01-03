package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.ItemNotFoundException;
import fontys.s3.individual.bookingsite.business.useCase.GetPropertyByIdUseCase;
import fontys.s3.individual.bookingsite.domain.dto.PropertyPageDTO;
import fontys.s3.individual.bookingsite.domain.response.GetPropertyByIdResponse;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyPictureEntity;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyPictureRepository;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetPropertyByIdUseCaseImp implements GetPropertyByIdUseCase
{
    private PropertyRepository propertyRepository;
    private PropertyPictureRepository propertyPictureRepository;

    @Override
    public GetPropertyByIdResponse getPropertyById(long id)
    {
        //get the propertyEntity and PropertyPictureEntity
        //build the dto
        //build and return response

        Optional<PropertyEntity> propertyEntity = propertyRepository.findById(id);

       if(propertyEntity.isPresent())
       {
           PropertyEntity property = propertyEntity.get();
           List<PropertyPictureEntity> propertyPictureEntities = propertyPictureRepository.findByPropertyEntityId(property.getId());

           List<String> otherPictures = propertyPictureEntities.stream()
                   .map(PropertyPictureEntity::getPhoto)
                   .toList();

           PropertyPageDTO propertyPageDTO = PropertyPageDTO.builder()
                   .propertyId(property.getId())
                   .name(property.getName())
                   .description(property.getDescription())
                   .location(property.getLocation())
                   .pricePerNight(property.getPricePerNight())
                   .mainPhoto(property.getMainPhoto())
                   .otherPhotos(otherPictures)
                   .build();

           GetPropertyByIdResponse response = GetPropertyByIdResponse.builder()
                   .propertyPageDTO(propertyPageDTO)
                   .build();

           return response;

       }
       else
       {
           throw new ItemNotFoundException("Property not found");
       }


    }
}
