//package fontys.s3.individual.bookingsite.persistence.repository;
//
//import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@ExtendWith(SpringExtension.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class UserRepositoryTest
//{
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void saveUserEntity_RequiredFieldsProvided_ShouldSaveTheUser()
//    {
//        // arrange
//        UserEntity userEntity = UserEntity.builder()
//                .username("thomas")
//                .password("password")
//                .build();
//        //act
//        userEntity = userRepository.save(userEntity);
//        UserEntity savedUser=entityManager.find(UserEntity.class, userEntity.getId());
//
//        UserEntity expected = UserEntity.builder()
//                .id(userEntity.getId())
//                .username(userEntity.getUsername())
//                .password(userEntity.getPassword())
//                .build();
//
//        assertEquals(expected, savedUser);
//
//    }
//}