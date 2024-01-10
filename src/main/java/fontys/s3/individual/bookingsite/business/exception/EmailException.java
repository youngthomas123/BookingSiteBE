package fontys.s3.individual.bookingsite.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailException extends ResponseStatusException
{
    public EmailException(String errorCause)
    {
        super(HttpStatus.INTERNAL_SERVER_ERROR, errorCause);
    }
}
