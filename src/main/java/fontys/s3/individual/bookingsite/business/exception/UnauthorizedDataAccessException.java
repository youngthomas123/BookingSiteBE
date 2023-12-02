package fontys.s3.individual.bookingsite.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorizedDataAccessException extends ResponseStatusException
{
    public UnauthorizedDataAccessException(String errorCause) {
        super(HttpStatus.FORBIDDEN, errorCause);
    }

}
