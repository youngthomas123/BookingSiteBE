package fontys.s3.individual.bookingsite.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePropertyEnlistingRequest
{

    private String propertyName;


    private String location;


    private double pricePerNight;     //between 1 and 1000


    private String description;


    private MultipartFile mainPicture; //One single image file(This is required)


    private List<MultipartFile> otherPhotos; //should be a list of images files (at least one required)
}
