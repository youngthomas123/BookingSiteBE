package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.domain.dto.UserDetailsDTO;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class GetUserByIdUseCaseImpTest
{
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserByIdUseCaseImp getUserByIdUseCase;

    @Test
    public void testGetUserById_UserExists() {

        long userId = 1L;
        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .username("user")
                .password("password")
                .type("landlord")
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));


        Optional<UserDetailsDTO> userDetailsDTO = getUserByIdUseCase.getUserById(userId);

        // Assertions
        assertTrue(userDetailsDTO.isPresent());
        UserDetailsDTO userDetails = userDetailsDTO.get();
        assertEquals("user", userDetails.getUsername());
        assertEquals("landlord", userDetails.getType());


        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testGetUserById_UserDoesNotExist() {

        long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());


        Optional<UserDetailsDTO> userDetailsDTO = getUserByIdUseCase.getUserById(userId);


        assertFalse(userDetailsDTO.isPresent());


        verify(userRepository, times(1)).findById(userId);
    }

}