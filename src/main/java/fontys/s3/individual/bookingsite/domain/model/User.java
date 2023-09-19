package fontys.s3.individual.bookingsite.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User
{
    private Long id;
    private String username;
    private String password;
    private String email;

}
