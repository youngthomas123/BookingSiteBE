package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.exception.UserNotFoundException;
import fontys.s3.individual.bookingsite.business.util.ImageStorageHelper;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.request.UpdateUserRequest;
import fontys.s3.individual.bookingsite.domain.response.UpdateUserResponse;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import org.h2.engine.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserByIdImpTest
{
    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessToken requestAccessToken;

    @Mock
    private ImageStorageHelper imageStorageHelper;


    @InjectMocks
    private UpdateUserByIdImp updateUserByIdImp;



    @Test
    public void updateUser_ValidUserIdAndUserIsPresentInDB_NoProfilePicProvided_ReturnsResponse()
    {
        long id = 1L;
        UpdateUserRequest request = UpdateUserRequest.builder()
                .bio("This is test bio")
                .email("test@gmail.com")
                .phoneNumber("123456789")
                .build();

        UserEntity user = UserEntity.builder()
                .id(id)
                .username("thomas")
                .password("$2a$12$cBD3hXzxCMSIEqqfXWv4ieJKbPUgOalXUWjLMoMyv8YUWnpglJCla")
                .type("landlord")
                .build();


        UserEntity savedUser = UserEntity.builder()
                .id(id)
                .username(user.getUsername())
                .password(user.getPassword())
                .type(user.getType())
                .bio(request.getBio())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .build();


        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(savedUser);

        //act
        UpdateUserResponse response = updateUserByIdImp.updateUser(request, id);

        //assert
        assertEquals(id, response.getId());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any());

    }

    @Test
    public void updateUser_ValidUserIdAndUserIsPresentInDB_ProfilePicIsProvided_ReturnsResponse()
    {
        long id = 1L;
        MultipartFile profileImageFile = Mockito.mock(MultipartFile.class);
        UpdateUserRequest request = UpdateUserRequest.builder()
                .bio("This is test bio")
                .email("test@gmail.com")
                .phoneNumber("123456789")
                .profilePic(profileImageFile)
                .build();

        UserEntity user = UserEntity.builder()
                .id(id)
                .username("thomas")
                .password("$2a$12$cBD3hXzxCMSIEqqfXWv4ieJKbPUgOalXUWjLMoMyv8YUWnpglJCla")
                .type("landlord")
                .build();

        UserEntity savedUser = UserEntity.builder()
                .id(id)
                .username(user.getUsername())
                .password(user.getPassword())
                .type(user.getType())
                .bio(request.getBio())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .build();

        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userRepository.findProfilePicById(user.getId())).thenReturn("ProfilePicUrl");

        when(imageStorageHelper.saveProfilePic(request.getProfilePic())).thenReturn("UpdatedProfilePicUrl");


        //act
        UpdateUserResponse response = updateUserByIdImp.updateUser(request, id);

        //assert
        assertEquals(id, response.getId());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any());
        verify(imageStorageHelper, times(1)).saveProfilePic(profileImageFile);


    }


    @Test
    public void updateUser_ValidUserIdButUserHasBeenDeletedFromDB_ThrowsException()
    {
        long id = 1L;
        UpdateUserRequest request = UpdateUserRequest.builder()
                .bio("This is test bio")
                .email("test@gmail.com")
                .phoneNumber("123456789")
                .build();


        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        //act and assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            updateUserByIdImp.updateUser(request, id); // This should throw the exception
        });


    }

    @Test
    public void updateUser_InvalidUserId_ThrowsException()
    {
        long id = 1L;
        UpdateUserRequest request = UpdateUserRequest.builder()
                .bio("This is test bio")
                .email("test@gmail.com")
                .phoneNumber("123456789")
                .build();

        when(requestAccessToken.getUserId()).thenReturn(5L);

        //act and assert
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> {
            updateUserByIdImp.updateUser(request, id); // This should throw the exception
        });
    }



}