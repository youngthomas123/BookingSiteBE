package fontys.s3.individual.bookingsite.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignUpDTO
{
    private String username;
    private String password;
    private String email;
}
