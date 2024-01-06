package fontys.s3.individual.bookingsite.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest
{
    private long propertyId;
    private String checkIn;
    private String checkOut;
}
