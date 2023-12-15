package fontys.s3.individual.bookingsite.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException
{
    public UserNotFoundException(String errorCause)
    {
        super(HttpStatus.NOT_FOUND, errorCause);
    }

}
