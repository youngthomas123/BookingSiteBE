package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.UserNameAlreadyExistsException;
import fontys.s3.individual.bookingsite.domain.request.CreateUserRequest;
import fontys.s3.individual.bookingsite.domain.response.CreateUserResponse;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImpTest
{
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserUseCaseImp createUserUseCase;

    @Test
    public void CreateGenericUser_ValidData_Success() {

        CreateUserRequest request = CreateUserRequest.builder()
                .username("testuser")
                .password("password")
                .build();
        UserEntity savedUser = UserEntity.builder().build();
        savedUser.setId(1L);


        when(userRepository.existsByUsername("testuser")).thenReturn(false);

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);


        CreateUserResponse response = createUserUseCase.createGenericUser(request);


        assertNotNull(response);
        assertEquals(1L, response.getId());


        verify(userRepository, times(1)).existsByUsername("testuser");

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void CreateGenericUser_UserNameExists_ThrowsException() {

        CreateUserRequest request = new CreateUserRequest("existinguser", "password");


        when(userRepository.existsByUsername("existinguser")).thenReturn(true);


        assertThrows(UserNameAlreadyExistsException.class, () ->
                createUserUseCase.createGenericUser(request));


        verify(userRepository, times(1)).existsByUsername("existinguser");

        verify(userRepository, never()).save(any(UserEntity.class));
    }

}