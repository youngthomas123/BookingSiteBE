package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.*;
import fontys.s3.individual.bookingsite.business.util.ImageStorageHelper;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyPictureEntity;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.BookingRepository;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyPictureRepository;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletePropertyByIdUseCaseImpTest
{
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private PropertyPictureRepository propertyPictureRepository;
    @Mock
    private AccessToken accessToken;
    @Mock
    private ImageStorageHelper imageStorageHelper;

    @InjectMocks
    private DeletePropertyByIdUseCaseImp deletePropertyByIdUseCaseImp;

    @Test
    void deletePropertyById_ValidData_DeletesProperty() {
        // Mock dependencies
        when(accessToken.hasRole("landlord")).thenReturn(true);

        UserEntity landlord = UserEntity.builder()
                .id(1L)
                .username("landlord1")
                .password("password")
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(landlord));

        PropertyEntity property = PropertyEntity.builder()
                .id(1L)
                .name("property1")
                .isEnlisted(false)
                .mainPhoto("mainPhoto")
                .build();
        when(propertyRepository.findByIdAndUserEntity(anyLong(), any(UserEntity.class))).thenReturn(Optional.of(property));

        List<PropertyPictureEntity> propertyPictureEntities = List.of(
                PropertyPictureEntity.builder().photo("photo1.jpg").build(),
                PropertyPictureEntity.builder().photo("photo2.jpg").build()
        );
        when(propertyPictureRepository.findByPropertyEntityId(anyLong())).thenReturn(propertyPictureEntities);

        when(bookingRepository.existsBookingsAfterTodayForProperty(anyLong(), any(LocalDate.class))).thenReturn(false);

        // Mock image storage helper behavior
        doNothing().when(imageStorageHelper).deleteMainPropertyPic(property.getMainPhoto());
        doNothing().when(imageStorageHelper).deleteOtherPropertyPhotos(anyList());

        // Mock repository delete operations
        doNothing().when(bookingRepository).deleteAllByPropertyEntityId(anyLong());
        doNothing().when(propertyPictureRepository).deleteAllByPropertyEntityId(anyLong());
        doNothing().when(propertyRepository).deleteById(anyLong());

        // Call the use case
        deletePropertyByIdUseCaseImp.deletePropertyById(1L);

        // Verify that the necessary methods were called
        verify(bookingRepository, times(1)).deleteAllByPropertyEntityId(anyLong());
        verify(propertyPictureRepository, times(1)).deleteAllByPropertyEntityId(anyLong());
        verify(propertyRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deletePropertyById_InvalidRole_ThrowsException()
    {
        when(accessToken.hasRole("landlord")).thenReturn(false);
        assertThrows(UnauthorizedDataAccessException.class, () ->
                deletePropertyByIdUseCaseImp.deletePropertyById(2L));
    }

    @Test
    void deletePropertyById_UserNotInDB_ThrowsException()
    {

        when(accessToken.hasRole("landlord")).thenReturn(true);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                deletePropertyByIdUseCaseImp.deletePropertyById(2L));
    }

    @Test
    void deletePropertyById_PropertyNotInDB_ThrowsException()
    {
        when(accessToken.hasRole("landlord")).thenReturn(true);

        UserEntity userEntity = UserEntity.builder()
                .username("thomas")
                .password("password")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        when(propertyRepository.findByIdAndUserEntity(2L, userEntity)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () ->
                deletePropertyByIdUseCaseImp.deletePropertyById(2L));
    }

    @Test
    void deletePropertyById_PropertyHasBookingsAfterToday_ThrowsException()
    {
        when(accessToken.hasRole("landlord")).thenReturn(true);

        UserEntity userEntity = UserEntity.builder()
                .username("thomas")
                .password("password")
                .build();

        PropertyEntity propertyEntity = PropertyEntity.builder()
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        when(propertyRepository.findByIdAndUserEntity(2L, userEntity)).thenReturn(Optional.of(propertyEntity));

        when(bookingRepository.existsBookingsAfterTodayForProperty(2L, LocalDate.now())).thenReturn(true);

        assertThrows(BookingConflictException.class, () ->
                deletePropertyByIdUseCaseImp.deletePropertyById(2L));


    }





}