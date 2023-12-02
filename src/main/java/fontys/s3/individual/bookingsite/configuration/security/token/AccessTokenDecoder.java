package fontys.s3.individual.bookingsite.configuration.security.token;

public interface AccessTokenDecoder
{
    AccessToken decode(String accessTokenEncoded);

}
