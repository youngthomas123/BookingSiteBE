package fontys.s3.individual.bookingsite.domain.request.UserRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest
{
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;


}
