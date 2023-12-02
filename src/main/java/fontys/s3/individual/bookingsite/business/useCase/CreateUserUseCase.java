package fontys.s3.individual.bookingsite.business.useCase;

import fontys.s3.individual.bookingsite.domain.request.CreateUserRequest;
import fontys.s3.individual.bookingsite.domain.response.CreateUserResponse;

public interface CreateUserUseCase
{

    CreateUserResponse createUser(CreateUserRequest request);

}
