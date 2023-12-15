package fontys.s3.individual.bookingsite.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidDatesException extends ResponseStatusException
{
    public InvalidDatesException()
    {
        super(HttpStatus.BAD_REQUEST, "INVALID_DATES");
    }
}
