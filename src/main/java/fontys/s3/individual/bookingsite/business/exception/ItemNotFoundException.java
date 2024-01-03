package fontys.s3.individual.bookingsite.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ItemNotFoundException extends ResponseStatusException
{
    public ItemNotFoundException(String errorCause)
    {
        super(HttpStatus.NOT_FOUND, errorCause);
    }
}
