package fontys.s3.individual.bookingsite.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisplayLandlordPropertyDTO
{
    private long id;
    private String propertyName;
    private String mainPhoto;
    private boolean enlisted;
    private boolean outstandingBooking;

}
