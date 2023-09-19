package fontys.s3.individual.bookingsite.domain.response.UserResponse;

import fontys.s3.individual.bookingsite.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUsersResponse
{
    private List<User> users;


}
