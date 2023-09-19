package fontys.s3.individual.bookingsite.persistence.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntity
{
    private Long id;
    private String username;
    private String password;
    private String email;

}
