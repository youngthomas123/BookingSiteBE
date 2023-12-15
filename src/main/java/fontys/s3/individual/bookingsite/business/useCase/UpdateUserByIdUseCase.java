package fontys.s3.individual.bookingsite.business.useCase;

import fontys.s3.individual.bookingsite.business.exception.UserNotFoundException;
import fontys.s3.individual.bookingsite.domain.request.UpdateUserRequest;
import fontys.s3.individual.bookingsite.domain.response.UpdateUserResponse;

public interface UpdateUserByIdUseCase
{
    UpdateUserResponse updateUser(UpdateUserRequest request, long id);
}
