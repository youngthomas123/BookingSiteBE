package fontys.s3.individual.bookingsite.business.useCase;

import fontys.s3.individual.bookingsite.domain.response.UpdatePropertyStatusResponse;

public interface UpdatePropertyStatusUseCase
{
    UpdatePropertyStatusResponse updatePropertyStatus(long propertyId, String status);
}
