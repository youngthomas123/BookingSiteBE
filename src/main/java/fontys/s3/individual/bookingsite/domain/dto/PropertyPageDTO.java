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
public class PropertyPageDTO
{
    private long propertyId;
    private String name;
    private String description;
    private String location;
    private double pricePerNight;
    private String mainPhoto;
    private List<String> otherPhotos;
    private boolean hasConflictingBookings;
}
