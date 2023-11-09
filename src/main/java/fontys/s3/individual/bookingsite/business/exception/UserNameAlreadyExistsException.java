package fontys.s3.individual.bookingsite.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.server.ResponseStatusException;

public class UserNameAlreadyExistsException extends ResponseStatusException
{
    public UserNameAlreadyExistsException()
    {
        super(HttpStatus.BAD_REQUEST, "UserName Already exists");
    }
}
