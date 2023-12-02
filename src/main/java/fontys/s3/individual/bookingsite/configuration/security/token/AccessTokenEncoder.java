package fontys.s3.individual.bookingsite.configuration.security.token;

public interface AccessTokenEncoder
{
    String encode(AccessToken accessToken);
}
