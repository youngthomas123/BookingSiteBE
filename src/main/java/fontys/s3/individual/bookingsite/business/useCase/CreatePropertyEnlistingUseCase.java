package fontys.s3.individual.bookingsite.business.useCase;

import fontys.s3.individual.bookingsite.domain.request.CreatePropertyEnlistingRequest;
import fontys.s3.individual.bookingsite.domain.request.CreateUserRequest;
import fontys.s3.individual.bookingsite.domain.response.CreatePropertyEnlistingResponse;

public interface CreatePropertyEnlistingUseCase
{
    CreatePropertyEnlistingResponse createPropertyEnlisting(CreatePropertyEnlistingRequest request);
}
