package fontys.s3.individual.bookingsite.business.useCase;

import fontys.s3.individual.bookingsite.domain.response.UpdateUserStatusResponse;

public interface UpdateUserStatusUseCase
{
    UpdateUserStatusResponse updateUserStatus(long userId, String status);
}
