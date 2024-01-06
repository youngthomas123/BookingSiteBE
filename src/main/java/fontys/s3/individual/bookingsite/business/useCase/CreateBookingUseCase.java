package fontys.s3.individual.bookingsite.business.useCase;

import fontys.s3.individual.bookingsite.domain.request.CreateBookingRequest;
import fontys.s3.individual.bookingsite.domain.response.CreateBookingResponse;

public interface CreateBookingUseCase
{
    CreateBookingResponse createBooking(CreateBookingRequest request);
}
