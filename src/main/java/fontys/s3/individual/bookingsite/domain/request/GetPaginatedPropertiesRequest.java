package fontys.s3.individual.bookingsite.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPaginatedPropertiesRequest
{
    private String location;
    private String checkIn;
    private String checkOut;
    private int currentPage;
    private int pageSize;


}
