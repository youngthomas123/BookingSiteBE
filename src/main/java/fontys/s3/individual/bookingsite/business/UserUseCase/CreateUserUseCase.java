package fontys.s3.individual.bookingsite.business.UserUseCase;

import fontys.s3.individual.bookingsite.domain.request.UserRequest.CreateUserRequest;
import fontys.s3.individual.bookingsite.domain.response.UserResponse.CreateUserResponse;

public interface CreateUserUseCase
{
    CreateUserResponse createUser (CreateUserRequest request);


}
