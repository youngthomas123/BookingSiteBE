package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.useCase.GetAllUsersUseCase;
import fontys.s3.individual.bookingsite.domain.dto.UserDetailsDTO;
import fontys.s3.individual.bookingsite.domain.dto.UserSignUpDTO;
import fontys.s3.individual.bookingsite.domain.response.GetAllUsersResponse;
import fontys.s3.individual.bookingsite.persistence.entity.LandLordEntity;
import fontys.s3.individual.bookingsite.persistence.entity.TenantEntity;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.LandLordRepository;
import fontys.s3.individual.bookingsite.persistence.repository.TenantRepository;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GetAllUsersUseCaseImp implements GetAllUsersUseCase
{
   private UserRepository userRepository;
    @Override
    public GetAllUsersResponse getAllUsers()
    {

        List<UserEntity> users = userRepository.findAll();

        List<UserDetailsDTO> userDtos = users.stream()
                .map(user -> UserDetailsDTO.builder()
                        .username(user.getUsername())
                        .id(user.getId())
                        .dateCreated(user.getDateCreated())
                        .type(user.getType())
                        .build())
                .collect(Collectors.toList());

        GetAllUsersResponse response = GetAllUsersResponse.builder()
                .users(userDtos)
                .build();

        return response;

    }
}
