package fontys.s3.individual.bookingsite.configuration.security.token;

import java.util.Set;

public interface AccessToken
{
    String getUsername();

    Long getUserId();

    Set<String> getRoles();

    boolean hasRole(String roleName);
}
