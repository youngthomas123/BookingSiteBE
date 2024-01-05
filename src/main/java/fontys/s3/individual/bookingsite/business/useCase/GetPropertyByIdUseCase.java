package fontys.s3.individual.bookingsite.business.useCase;

import fontys.s3.individual.bookingsite.domain.response.GetPaginatedPropertiesResponse;
import fontys.s3.individual.bookingsite.domain.response.GetPropertyByIdResponse;

public interface GetPropertyByIdUseCase
{
    GetPropertyByIdResponse getPropertyById(long id, String checkin, String checkout);

}
