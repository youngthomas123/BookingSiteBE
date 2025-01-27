package fontys.s3.individual.bookingsite.domain.response;


import fontys.s3.individual.bookingsite.domain.dto.PropertyPageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPropertyByIdResponse
{
    private PropertyPageDTO propertyPageDTO;

}
