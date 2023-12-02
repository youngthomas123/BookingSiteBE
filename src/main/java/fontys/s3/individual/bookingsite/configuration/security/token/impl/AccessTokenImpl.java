package fontys.s3.individual.bookingsite.configuration.security.token.impl;

import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode
@Getter
public class AccessTokenImpl implements AccessToken
{
    private final String username;
    private final Long userId;
    private final Set<String> roles;

    public AccessTokenImpl(String username, Long userId, Collection<String> roles) {
        this.username = username;
        this.userId = userId;
        this.roles = roles != null ? Set.copyOf(roles) : Collections.emptySet();
    }

    @Override
    public boolean hasRole(String roleName)
    {
        return this.roles.contains(roleName);
    }
}
