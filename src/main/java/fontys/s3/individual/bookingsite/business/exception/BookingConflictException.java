package fontys.s3.individual.bookingsite.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BookingConflictException extends ResponseStatusException
{
    public BookingConflictException(String errorCause)
    {
        super(HttpStatus.CONFLICT, errorCause);
    }
}
