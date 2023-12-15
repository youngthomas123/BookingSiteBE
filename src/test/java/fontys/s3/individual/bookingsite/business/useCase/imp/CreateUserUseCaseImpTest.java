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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImpTest
{
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCaseImp createUserUseCase;



    @Test
    public void CreateUser_ValidData_Success() {

        CreateUserRequest request = CreateUserRequest.builder()
                .username("testuser")
                .password("password")
                .type("tenant")
                .build();

        UserEntity user = UserEntity.builder()
                .id(1L)
                .username(request.getUsername())
                .password(request.getPassword())
                .type(request.getType())
                .build();



        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);

        when(passwordEncoder.encode(request.getPassword())).thenReturn("$2a$12$6JY34K3328GqJ5ZE6pNiru1cpZ/WnUQIQq1Ig0CSKDgv1V/pscNyG");

        when(userRepository.save(any(UserEntity.class))).thenReturn(user);


        CreateUserResponse response = createUserUseCase.createUser(request);


        assertNotNull(response);
        assertEquals(1L, response.getId());


        verify(userRepository, times(1)).existsByUsername("testuser");

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void CreateUser_UserNameExists_ThrowsException()
    {

        CreateUserRequest request =  CreateUserRequest.builder()
                .username("existinguser")
                .password("password")
                .type("tenant")
                .build();


        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);


        assertThrows(UserNameAlreadyExistsException.class, () ->
                createUserUseCase.createUser(request));


        verify(userRepository, times(1)).existsByUsername(request.getUsername());

        verify(userRepository, never()).save(any(UserEntity.class));
    }

}