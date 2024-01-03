package fontys.s3.individual.bookingsite.domain.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest
{
    @Length(min = 3, max = 15)
    private String phoneNumber;


    @Length(min = 2, max = 30)
    @Email
    private String email;

    @Length(min = 0, max = 200)
    private String bio;

    private MultipartFile profilePic;
}
