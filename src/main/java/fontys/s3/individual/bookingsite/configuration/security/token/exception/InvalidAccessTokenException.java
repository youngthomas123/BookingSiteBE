package fontys.s3.individual.bookingsite.configuration.security.token.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidAccessTokenException extends ResponseStatusException
{
    public InvalidAccessTokenException(String errorCause) {
        super(HttpStatus.UNAUTHORIZED, errorCause);
    }

}
