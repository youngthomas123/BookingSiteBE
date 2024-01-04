package fontys.s3.individual.bookingsite.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException
{
    public BadRequestException(String errorCause)
    {
        super(HttpStatus.BAD_REQUEST, errorCause);
    }
}
