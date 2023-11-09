//package fontys.s3.individual.bookingsite.business.useCase.imp;
//
//import fontys.s3.individual.bookingsite.business.exception.UserNameAlreadyExistsException;
//import fontys.s3.individual.bookingsite.domain.request.CreateUserRequest;
//import fontys.s3.individual.bookingsite.domain.response.CreateUserResponse;
//import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
//import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class CreateUserUseCaseImpTest
//{
//    @Mock
//    private UserRepository userRepositoryMock;
//
//    @InjectMocks
//    private CreateUserUseCaseImp createUserUseCaseImp;
//
//    @Test
//    void createUser_ValidInput_SavesTheUser()
//    {
//        // arrange
//        CreateUserRequest request = CreateUserRequest.builder()
//                .username("Thomas")
//                .email("Thomas@gmail.com")
//                .build();
//
//        UserEntity userEntity = UserEntity.builder()
//                .id(1L)
//                .build();
//
//        when(userRepositoryMock.existsByUserName(request.getUsername())).thenReturn(false);
//        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
//        when(userRepositoryMock.save(userEntityCaptor.capture())).thenReturn(userEntity);
//
//
//        // act
//        CreateUserResponse response = createUserUseCaseImp.createUser(request);
//
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(1L, response.getId());
//
//        // Verify that the correct UserEntity was saved
//        UserEntity capturedUserEntity = userEntityCaptor.getValue();
//        assertEquals(request.getUsername(), capturedUserEntity.getUsername());
//        assertEquals(request.getPassword(), capturedUserEntity.getPassword());
//        assertEquals(request.getEmail(), capturedUserEntity.getEmail());
//    }
//
//
//    @Test
//    void createUser_InvalidInput_ThrowsException()
//    {
//        // arrange
//        CreateUserRequest request = CreateUserRequest.builder()
//                .username("Thomas")
//                .email("Thomas@gmail.com")
//                .password("Password")
//                .build();
//
//        when(userRepositoryMock.existsByUserName(request.getUsername())).thenReturn(true);
//
//        //act and assert
//
//        assertThrows(UserNameAlreadyExistsException.class, () ->
//            createUserUseCaseImp.createUser(request));
//
//    }
//}