package fontys.s3.individual.bookingsite.business.useCase;

import fontys.s3.individual.bookingsite.domain.response.GetPaginatedPropertiesResponse;

import fontys.s3.individual.bookingsite.domain.request.GetPaginatedPropertiesRequest;
import java.util.Optional;

public interface GetPaginatedPropertiesUseCase
{
    Optional<GetPaginatedPropertiesResponse>getPaginatedProperties(GetPaginatedPropertiesRequest request);
}
