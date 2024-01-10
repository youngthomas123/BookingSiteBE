package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.exception.UserNotFoundException;
import fontys.s3.individual.bookingsite.business.util.ImageStorageHelper;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.request.CreatePropertyEnlistingRequest;
import fontys.s3.individual.bookingsite.domain.response.CreatePropertyEnlistingResponse;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyPictureRepository;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePropertyEnlistingUseCaseImpTest
{
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private ImageStorageHelper imageStorageHelper;
    @Mock
    private AccessToken requestAccessToken;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PropertyPictureRepository propertyPictureRepository;

    @InjectMocks
    private CreatePropertyEnlistingUseCaseImp createPropertyEnlistingUseCaseImp;

    @Test
    public void createPropertyEnlisting_InvalidRole_ThrowsException()
    {
        when(requestAccessToken.hasRole("landlord")).thenReturn(false);

        CreatePropertyEnlistingRequest request = CreatePropertyEnlistingRequest.builder().build();
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> {
            createPropertyEnlistingUseCaseImp.createPropertyEnlisting(request); // This should throw the exception
        });

    }

    @Test
    public void createPropertyEnlisting_UserNotInDB_ThrowsException()
    {
        when(requestAccessToken.hasRole("landlord")).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        CreatePropertyEnlistingRequest request = CreatePropertyEnlistingRequest.builder().build();

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            createPropertyEnlistingUseCaseImp.createPropertyEnlisting(request); // This should throw the exception
        });

    }

    @Test
    public void createPropertyEnlisting_ValidData_ReturnsResponse() {

        // Mock dependencies
        when(requestAccessToken.hasRole("landlord")).thenReturn(true);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("jonny")
                .password("password")
                .build();
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(userEntity));

        // Mock MultipartFile for main photo
        MockMultipartFile mainPhoto = new MockMultipartFile("mainPhoto", "mainPhoto.jpg", "image/jpeg", "mainPhotoContent".getBytes());

        // Mock MultipartFile for other photo
        MockMultipartFile otherPhoto = new MockMultipartFile("otherPhoto", "otherPhoto.jpg", "image/jpeg", "otherPhotoContent".getBytes());

        CreatePropertyEnlistingRequest request = CreatePropertyEnlistingRequest.builder()
                .mainPicture(mainPhoto)
                .propertyName("property1")
                .pricePerNight(100.0)
                .location("location1")
                .description("description1")
                .otherPhotos(Collections.singletonList(otherPhoto))
                .build();

        // Mock image storage helper behavior
        when(imageStorageHelper.saveMainPropertyPic(mainPhoto)).thenReturn("mainPictureUrl");
        when(imageStorageHelper.saveOtherPropertyPhotos(Collections.singletonList(otherPhoto))).thenReturn(Collections.singletonList("otherPhotoUrl"));

        // Mock property repository behavior
        PropertyEntity savedPropertyEntity = PropertyEntity.builder()
                .id(1L)
                .mainPhoto("mainPictureUrl")
                .name("property1")
                .pricePerNight(100.0)
                .location("location1")
                .description("description1")
                .userEntity(userEntity)
                .isEnlisted(true)
                .build();
        when(propertyRepository.save(any())).thenReturn(savedPropertyEntity);


        when(propertyPictureRepository.save(any())).thenReturn(null);


        CreatePropertyEnlistingResponse response = createPropertyEnlistingUseCaseImp.createPropertyEnlisting(request);

        // Assertions
        assertEquals(savedPropertyEntity.getId(), response.getPropertyId());


    }


}