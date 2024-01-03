package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.exception.UserNotFoundException;
import fontys.s3.individual.bookingsite.business.useCase.CreatePropertyEnlistingUseCase;
import fontys.s3.individual.bookingsite.business.util.ImageStorageHelper;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.request.CreatePropertyEnlistingRequest;
import fontys.s3.individual.bookingsite.domain.response.CreatePropertyEnlistingResponse;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyPictureEntity;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyPictureRepository;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreatePropertyEnlistingUseCaseImp implements CreatePropertyEnlistingUseCase
{
    private PropertyRepository propertyRepository;
    private ImageStorageHelper imageStorageHelper;
    private AccessToken requestAccessToken;
    private UserRepository userRepository;
    private PropertyPictureRepository propertyPictureRepository;
    @Override
    public CreatePropertyEnlistingResponse createPropertyEnlisting(CreatePropertyEnlistingRequest request)
    {
        // only landlords allowed to create enlisting
        //get all the data about the property
        //create method in imageHelper to store main picture and other photos.
        //get url from image helper for MainPicture
        //create a PropertyEntity
        //save it in DB
        //create n number of PropertyPictureEntities(n=number of other photos)
        //save it in DB
        //response saves the PropertyId
        if(requestAccessToken.hasRole("landlord"))
        {
            String mainPropertyPictureUrl = imageStorageHelper.saveMainPropertyPic(request.getMainPicture());

            Optional<UserEntity> userEntity = userRepository.findById(requestAccessToken.getUserId());

            if(userEntity.isPresent())
            {
                UserEntity user = userEntity.get();
                PropertyEntity propertyEntity = PropertyEntity.builder()
                        .mainPhoto(mainPropertyPictureUrl)
                        .name(request.getPropertyName())
                        .pricePerNight(request.getPricePerNight())
                        .location(request.getLocation())
                        .description(request.getDescription())
                        .userEntity(user)
                        .build();

                PropertyEntity savedPropertyEntity = propertyRepository.save(propertyEntity);

                //take every other photo and either send post requests individually to image server of together.
                //If together, it will return a list or urls, create propertyPictureEntities from there.
                List<String>otherPropertyPhotosUrls = imageStorageHelper.saveOtherPropertyPhotos(request.getOtherPhotos());

                otherPropertyPhotosUrls.forEach(url ->{
                    PropertyPictureEntity propertyPictureEntity = PropertyPictureEntity.builder()
                            .propertyEntity(savedPropertyEntity)
                            .photo(url)
                            .build();

                    propertyPictureRepository.save(propertyPictureEntity);

                });



                CreatePropertyEnlistingResponse response = CreatePropertyEnlistingResponse.builder()
                        .propertyId(savedPropertyEntity.getId())
                        .build();
                return response;

            }
            else
            {
                throw new UserNotFoundException("The Landlord was not found in DB");
            }

        }
        else
        {
            throw new UnauthorizedDataAccessException("Invalid user role");
        }



    }
}
