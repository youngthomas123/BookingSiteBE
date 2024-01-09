package fontys.s3.individual.bookingsite.business.util;

import fontys.s3.individual.bookingsite.persistence.entity.BookingEntity;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailHelper
{

    private JavaMailSender emailSender;


    @Async
    public void sendBookingConfirmationEmail(String recipientEmail, BookingEntity booking, PropertyEntity property)
            throws jakarta.mail.MessagingException
    {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(recipientEmail);
        helper.setSubject("Booking Confirmation");


        String emailContent = buildEmailContent(booking, property);

        helper.setText(emailContent);



        emailSender.send(message);
    }


    private String buildEmailContent(BookingEntity booking, PropertyEntity property)
    {
        StringBuilder content = new StringBuilder();
        content.append("Booking Confirmation Details:\n\n");
        content.append("Property Name: ").append(property.getName()).append("\n");
        content.append("Property Location: ").append(property.getLocation()).append("\n");
        content.append("Booking ID: ").append(booking.getId()).append("\n");
        content.append("Check-In: ").append(booking.getCheckIn()).append("\n");
        content.append("Check-Out: ").append(booking.getCheckOut()).append("\n");
        // Add more details as needed...

        return content.toString();
    }




}
