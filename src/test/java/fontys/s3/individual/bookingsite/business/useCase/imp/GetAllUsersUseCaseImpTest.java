package fontys.s3.individual.bookingsite.business.useCase.imp;

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

    @InjectMocks
    private GetAllUsersUseCaseImp getAllUsersUseCase;

    @Test
    public void GetAllUsers_ReturnsResponseWithAllUsers() {

        UserEntity user1 = UserEntity.builder()
                .id(1L)
                .username("user1")
                .password("password1")
                .type("tenant")
                .build();
        UserEntity user2 = UserEntity.builder()
                .username("user2")
                .password("password2")
                .type("landlord")
                .build();
        List<UserEntity> mockUsers = Arrays.asList(user1, user2);


        when(userRepository.findAll()).thenReturn(mockUsers);


        GetAllUsersResponse response = getAllUsersUseCase.getAllUsers();


        assertNotNull(response);
        assertEquals(2, response.getUsers().size());




        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void GetAllUsers_ReturnsResponseWithEmptyList() {

        when(userRepository.findAll()).thenReturn(Collections.emptyList());


        GetAllUsersResponse response = getAllUsersUseCase.getAllUsers();


        assertNotNull(response);
        assertTrue(response.getUsers().isEmpty());


        verify(userRepository, times(1)).findAll();
    }

}