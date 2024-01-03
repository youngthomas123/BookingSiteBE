package fontys.s3.individual.bookingsite.domain.response;

import fontys.s3.individual.bookingsite.domain.dto.DisplayLandlordPropertyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetLandlordPropertiesByIdResponse
{
    List<DisplayLandlordPropertyDTO>properties;
}
