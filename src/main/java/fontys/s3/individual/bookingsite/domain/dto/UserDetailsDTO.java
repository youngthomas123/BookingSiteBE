package fontys.s3.individual.bookingsite.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsDTO
{
    private String username;
    private LocalDateTime dateCreated;
    private String type;

    //new
    private String bio;
    private String phoneNumber;
    private String email;

}
