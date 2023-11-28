package fontys.s3.individual.bookingsite.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyHomePageDTO
{
   private Long propertyId;
   private String description;
   private String name;
   private Double priceForNight;
   private Long landlordId;
   private String mainPhoto;
}
