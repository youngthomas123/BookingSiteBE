package fontys.s3.individual.bookingsite.TestConfig;

import fontys.s3.individual.bookingsite.business.util.EmailHelper;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@TestConfiguration
public class TestEmailHelperConfig {

    @Bean
    public EmailHelper emailHelper(JavaMailSender javaMailSender) {
        return new EmailHelper(javaMailSender);
    }

    @Bean
    public JavaMailSender javaMailSender() {
        return Mockito.mock(JavaMailSender.class);
    }
}
