package fontys.s3.individual.bookingsite.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePropertyStatusResponse
{
    private long propertyId;
    private boolean enlisted;
}
