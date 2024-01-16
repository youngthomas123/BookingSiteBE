package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.useCase.GetAllUsersUseCase;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.dto.UserDetailsDTO;
import fontys.s3.individual.bookingsite.domain.response.GetAllUsersResponse;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
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
    private AccessToken requestAccessToken;
    @Override
    public GetAllUsersResponse getAllUsers()
    {
        if(requestAccessToken.hasRole("admin"))
        {
            List<UserEntity> users = userRepository.findAll();

            List<UserDetailsDTO> userDtos = users.stream()
                    .map(user -> UserDetailsDTO.builder()
                            .userId(user.getId())
                            .username(user.getUsername())
                            .dateCreated(user.getDateCreated())
                            .type(user.getType())
                            .isBanned(user.isBanned())
                            .profilePicUrl(user.getProfilePic())
                            .dateCreated(user.getDateCreated())
                            .build())
                    .collect(Collectors.toList());

            GetAllUsersResponse response = GetAllUsersResponse.builder()
                    .users(userDtos)
                    .build();

            return response;
        }
        else
        {
            throw new UnauthorizedDataAccessException("Invalid user role");
        }



    }
}
