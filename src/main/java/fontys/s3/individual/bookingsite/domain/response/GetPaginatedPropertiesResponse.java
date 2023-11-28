package fontys.s3.individual.bookingsite.domain.response;

import fontys.s3.individual.bookingsite.domain.dto.PropertyHomePageDTO;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPaginatedPropertiesResponse
{
    List<PropertyHomePageDTO> Properties;
    long totalCount;

}
