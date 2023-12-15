package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.response.GetAllUsersResponse;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllUsersUseCaseImpTest
{
    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessToken requestAccessToken;

    @InjectMocks
    private GetAllUsersUseCaseImp getAllUsersUseCase;

    @Test
    public void GetAllUsers_HasValidRole_ReturnsResponse()
    {
        //arrange

        UserEntity user1 = UserEntity.builder()
                .id(1L)
                .username("user1")
                .password("password1")
                .type("tenant")
                .build();
        UserEntity user2 = UserEntity.builder()
                .id(2L)
                .username("user2")
                .password("password2")
                .type("landlord")
                .build();

        List<UserEntity> mockUsers = Arrays.asList(user1, user2);

        // act
        when(requestAccessToken.hasRole("admin")).thenReturn(true);
        when(userRepository.findAll()).thenReturn(mockUsers);
        GetAllUsersResponse response = getAllUsersUseCase.getAllUsers();

        //assert
        assertNotNull(response);
        assertEquals(2, response.getUsers().size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void GetAllUsers_InvalidRole_ThrowsException()
    {
        //arrange

        //act and assert
        when(requestAccessToken.hasRole("admin")).thenReturn(false);

        //has to throw UnauthorizedDataAccessException
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> {
            getAllUsersUseCase.getAllUsers(); // This should throw the exception
        });

    }

}