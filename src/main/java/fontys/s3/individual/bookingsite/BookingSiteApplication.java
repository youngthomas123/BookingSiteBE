package fontys.s3.individual.bookingsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BookingSiteApplication {

    //test

    public static void main(String[] args) {
        SpringApplication.run(BookingSiteApplication.class, args);
    }

}
