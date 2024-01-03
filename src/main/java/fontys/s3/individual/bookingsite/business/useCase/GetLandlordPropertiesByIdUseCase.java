package fontys.s3.individual.bookingsite.business.useCase;

import fontys.s3.individual.bookingsite.domain.response.GetLandlordPropertiesByIdResponse;

public interface GetLandlordPropertiesByIdUseCase
{
    GetLandlordPropertiesByIdResponse getProperties(long id);
}
