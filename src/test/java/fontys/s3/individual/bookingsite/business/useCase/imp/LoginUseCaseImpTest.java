package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.InvalidCredentialsException;
import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.exception.UserNotFoundException;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessTokenEncoder;
import fontys.s3.individual.bookingsite.domain.request.LoginRequest;
import fontys.s3.individual.bookingsite.domain.response.LoginResponse;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImpTest
{
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @InjectMocks
    private LoginUseCaseImp loginUseCaseImp;

    @Test
    public void login_ValidCredentials_ReturnsResponse()
    {
        //arrange
        LoginRequest request = LoginRequest.builder()
                .username("thomas")
                .password("password")
                .build();

        UserEntity user = UserEntity.builder()
                .id(1L)
                .username("thomas")
                .password("$2a$12$3YisSUfkf7ctqc6MfOZQ6.ggVP0t4pIB0isLiPPDDC1tZOztPhOZy")
                .type("landlord")
                .build();


        when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
        when(passwordEncoder.matches(request.getPassword(),user.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(any())).thenReturn("accessToken");

        //act
        LoginResponse response = loginUseCaseImp.login(request);

        //assert
        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());

    }

    @Test
    public void login_UsernameDoesNotExist_ThrowsException()
    {
        //arrange
        LoginRequest request = LoginRequest.builder()
                .username("thomas")
                .password("password")
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(null);

        //assert
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            loginUseCaseImp.login(request); // This should throw the exception
        });






    }

    @Test
    public void login_PasswordDoesNotMatch_ThrowsException()
    {
        LoginRequest request = LoginRequest.builder()
                .username("thomas")
                .password("password")
                .build();

        UserEntity user = UserEntity.builder()
                .id(1L)
                .username("thomas")
                .password("$2a$12$3YisSUfkf7ctqc6MfOZQ6.ggVP0t4pIB0isLiPPDDC1tZOztPhOZy")
                .type("landlord")
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
        when(passwordEncoder.matches(request.getPassword(),user.getPassword())).thenReturn(false);

        //assert
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> {
            loginUseCaseImp.login(request); // This should throw the exception
        });


    }

    @Test
    public void login_UserIsBanned_ThrowsException()
    {
        LoginRequest request = LoginRequest.builder()
                .username("thomas")
                .password("password")
                .build();

        UserEntity user = UserEntity.builder()
                .id(1L)
                .username("thomas")
                .password("$2a$12$3YisSUfkf7ctqc6MfOZQ6.ggVP0t4pIB0isLiPPDDC1tZOztPhOZy")
                .type("landlord")
                .isBanned(true)
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(user);
        when(passwordEncoder.matches(request.getPassword(),user.getPassword())).thenReturn(true);

        //assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            loginUseCaseImp.login(request); // This should throw the exception
        });
    }



}