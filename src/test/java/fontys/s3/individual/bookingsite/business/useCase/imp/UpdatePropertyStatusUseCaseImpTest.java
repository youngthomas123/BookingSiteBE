package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.ItemNotFoundException;
import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.exception.UserNotFoundException;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.response.UpdatePropertyStatusResponse;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdatePropertyStatusUseCaseImpTest
{
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccessToken requestAccessToken;

    @InjectMocks
    private UpdatePropertyStatusUseCaseImp updatePropertyStatusUseCaseImp;

    @Test
    void updatePropertyStatus_ValidInputToEnlist_ReturnsResponse()
    {
        // Arrange
        when(requestAccessToken.hasRole("landlord")).thenReturn(true);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("landlord")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        PropertyEntity propertyEntity = PropertyEntity.builder()
                .id(2L)
                .name("property")
                .isEnlisted(false)
                .userEntity(userEntity)
                .build();

        when(propertyRepository.findByIdAndUserEntity(anyLong(), any(UserEntity.class))).thenReturn(Optional.of(propertyEntity));

        // Mock propertyRepository.save to return null
        when(propertyRepository.save(any(PropertyEntity.class))).thenReturn(propertyEntity);

       UpdatePropertyStatusResponse reponse= updatePropertyStatusUseCaseImp.updatePropertyStatus(2L, "enlist");



        // Verify that the necessary methods were called
        verify(propertyRepository, times(1)).findByIdAndUserEntity(anyLong(), any(UserEntity.class));
        verify(propertyRepository, times(1)).save(any(PropertyEntity.class));
        assertEquals(2L, reponse.getPropertyId());
        assertTrue(reponse.isEnlisted());
    }

    @Test
    void updatePropertyStatus_ValidInputToDelist_ReturnsResponse()
    {
        // Arrange
        when(requestAccessToken.hasRole("landlord")).thenReturn(true);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("landlord")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

        PropertyEntity propertyEntity = PropertyEntity.builder()
                .id(2L)
                .name("property")
                .isEnlisted(false)
                .userEntity(userEntity)
                .build();

        when(propertyRepository.findByIdAndUserEntity(anyLong(), any(UserEntity.class))).thenReturn(Optional.of(propertyEntity));

        // Mock propertyRepository.save to return null
        when(propertyRepository.save(any(PropertyEntity.class))).thenReturn(propertyEntity);

        UpdatePropertyStatusResponse reponse= updatePropertyStatusUseCaseImp.updatePropertyStatus(2L, "delist");



        // Verify that the necessary methods were called
        verify(propertyRepository, times(1)).findByIdAndUserEntity(anyLong(), any(UserEntity.class));
        verify(propertyRepository, times(1)).save(any(PropertyEntity.class));
        assertEquals(2L, reponse.getPropertyId());
        assertFalse(reponse.isEnlisted());
    }

    @Test
    void updatePropertyStatus_InvalidRole_ThrowsException()
    {
        when(requestAccessToken.hasRole("landlord")).thenReturn(false);

        assertThrows(UnauthorizedDataAccessException.class, () ->
                updatePropertyStatusUseCaseImp.updatePropertyStatus(2L, "enlist"));


    }

    @Test
    void updatePropertyStatus_UserNotFoundInDB_ThrowsException()
    {
        when(requestAccessToken.hasRole("landlord")).thenReturn(true);
        when(requestAccessToken.getUserId()).thenReturn(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(UserNotFoundException.class, () ->
                updatePropertyStatusUseCaseImp.updatePropertyStatus(2L, "enlist"));



    }

    @Test
    void updatePropertyStatus_PropertyNotInDB_ThrowsException()
    {
        when(requestAccessToken.hasRole("landlord")).thenReturn(true);
        when(requestAccessToken.getUserId()).thenReturn(1L);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        when(propertyRepository.findByIdAndUserEntity(1L, userEntity)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () ->
                updatePropertyStatusUseCaseImp.updatePropertyStatus(1L, "enlist"));




    }




}