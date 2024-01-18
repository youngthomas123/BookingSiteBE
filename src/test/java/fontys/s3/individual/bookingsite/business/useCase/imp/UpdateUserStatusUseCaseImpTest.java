package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.BadRequestException;
import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.exception.UserNotFoundException;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.response.UpdateUserStatusResponse;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserStatusUseCaseImpTest
{
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccessToken requestAccessToken;
    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private UpdateUserStatusUseCaseImp updateUserStatusUseCaseImp;

    @Test
    public void updateUserStatus_ValidInputBanLandlord_ReturnsResponse()
    {
        // Arrange
        when(requestAccessToken.hasRole("admin")).thenReturn(true);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .type("landlord")
                .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any())).thenReturn(userEntity);

        // Act
        UpdateUserStatusResponse response = updateUserStatusUseCaseImp.updateUserStatus(1L, "ban");

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertTrue(response.isBanned());

        // Verify userRepository methods are called
        verify(userRepository).findById(1L);
        verify(userRepository).save(any());
        verify(propertyRepository).setAllPropertiesNotEnlistedForUser(userEntity);

        // Ensure that propertyRepository.setAllPropertiesNotEnlistedForUser is called only when the user is a landlord
        if ("landlord".equals(userEntity.getType())) {
            verify(propertyRepository).setAllPropertiesNotEnlistedForUser(userEntity);
        } else {
            verify(propertyRepository, never()).setAllPropertiesNotEnlistedForUser(any());
        }
    }

    @Test
    public void updateUserStatus_ValidInputBanTenant_ReturnsResponse()
    {
        // Arrange
        when(requestAccessToken.hasRole("admin")).thenReturn(true);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .type("tenant")
                .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any())).thenReturn(userEntity);

        // Act
        UpdateUserStatusResponse response = updateUserStatusUseCaseImp.updateUserStatus(1L, "ban");

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertTrue(response.isBanned());

        // Verify userRepository methods are called
        verify(userRepository).findById(1L);
        verify(userRepository).save(any());

        // Ensure that propertyRepository.setAllPropertiesNotEnlistedForUser is called only when the user is a landlord
        if ("landlord".equals(userEntity.getType())) {
            verify(propertyRepository).setAllPropertiesNotEnlistedForUser(userEntity);
        } else {
            verify(propertyRepository, never()).setAllPropertiesNotEnlistedForUser(any());
        }
    }

    @Test
    public void updateUserStatus_ValidInputUnBanUser_ReturnsResponse()
    {
        // Arrange
        when(requestAccessToken.hasRole("admin")).thenReturn(true);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .type("tenant")
                .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any())).thenReturn(userEntity);

        // Act
        UpdateUserStatusResponse response = updateUserStatusUseCaseImp.updateUserStatus(1L, "unban");

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertFalse(response.isBanned());

        // Verify userRepository methods are called
        verify(userRepository).findById(1L);
        verify(userRepository).save(any());


    }

    @Test
    public void updateUserStatus_InvalidStatusInRequest_ThrowsException()
    {
        when(requestAccessToken.hasRole("admin")).thenReturn(true);

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .type("tenant")
                .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));

        assertThrows(BadRequestException.class, () ->
                updateUserStatusUseCaseImp.updateUserStatus(1L, "invalidStatus"));

    }

    @Test
    public void updateUserStatus_UserNotInDB_ThrowsException()
    {
        when(requestAccessToken.hasRole("admin")).thenReturn(true);


        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                updateUserStatusUseCaseImp.updateUserStatus(1L, "ban"));


    }

    @Test
    public void updateUserStatus_InvalidRole_ThrowsException()
    {
        when(requestAccessToken.hasRole("admin")).thenReturn(false);
        assertThrows(UnauthorizedDataAccessException.class, () ->
                updateUserStatusUseCaseImp.updateUserStatus(1L, "ban"));


    }





}