package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.InvalidCredentialsException;
import fontys.s3.individual.bookingsite.business.useCase.LoginUseCase;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessTokenEncoder;
import fontys.s3.individual.bookingsite.configuration.security.token.impl.AccessTokenImpl;
import fontys.s3.individual.bookingsite.domain.request.LoginRequest;
import fontys.s3.individual.bookingsite.domain.response.LoginResponse;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class LoginUseCaseImp implements LoginUseCase
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;


    @Override
    public LoginResponse login(LoginRequest loginRequest)
    {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null)
        {
            throw new InvalidCredentialsException();
        }

        if (!matchesPassword(loginRequest.getPassword(), user.getPassword()))
        {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        return LoginResponse.builder()
                .accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {

        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(UserEntity user)
    {


        List<String>roles = new ArrayList<>();
        roles.add(user.getType());

        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getUsername(), user.getId(), roles));
    }


}
