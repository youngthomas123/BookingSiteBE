package fontys.s3.individual.bookingsite.business.useCase;

import fontys.s3.individual.bookingsite.domain.request.LoginRequest;
import fontys.s3.individual.bookingsite.domain.response.LoginResponse;

public interface LoginUseCase
{
    LoginResponse login(LoginRequest loginRequest);


}
