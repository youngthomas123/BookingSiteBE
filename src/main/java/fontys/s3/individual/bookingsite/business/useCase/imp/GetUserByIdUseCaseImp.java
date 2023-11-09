package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.useCase.GetUserByIdUseCase;
import fontys.s3.individual.bookingsite.domain.dto.UserDetailsDTO;
import fontys.s3.individual.bookingsite.domain.dto.UserSignUpDTO;
import fontys.s3.individual.bookingsite.persistence.entity.LandLordEntity;
import fontys.s3.individual.bookingsite.persistence.entity.TenantEntity;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.LandLordRepository;
import fontys.s3.individual.bookingsite.persistence.repository.TenantRepository;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetUserByIdUseCaseImp implements GetUserByIdUseCase
{
    private UserRepository userRepository;

    @Override
    public Optional<UserDetailsDTO> getUserById(long userId)
    {

        Optional<UserEntity> userEntity = userRepository.findById(userId);

        if (userEntity.isPresent() )
        {
            UserEntity user = userEntity.get();  // Get the actual UserEntity object

            UserDetailsDTO userDetailsDTO = UserDetailsDTO.builder()
                    .username(user.getUsername())
                    .id(user.getId())
                    .dateCreated(user.getDateCreated())
                    .type(user.getType())
                    .build();

            return Optional.of(userDetailsDTO);

        }
        else{
            return Optional.empty(); // User not found, return an empty Optional
        }
    }



}
