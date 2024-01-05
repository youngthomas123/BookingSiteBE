package fontys.s3.individual.bookingsite.business.util;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
@Component
public class DateValidator
{
    String format = "yyyy-MM-dd";
    public  boolean areDatesValid(String checkin, String checkout)
    {
        if (checkin != null && !checkin.trim().isEmpty() && checkout != null && !checkout.trim().isEmpty())
        {
            try
            {
                // Define the date format expected in the string
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

                // Parse the string into a LocalDate using the defined format
                LocalDate checkIn = LocalDate.parse(checkin, formatter);
                LocalDate checkOut = LocalDate.parse(checkout, formatter);

                return checkIn.isBefore(checkOut);

            }
            catch (DateTimeParseException e)
            {
                // If parsing fails, handle the exception or simply return false
                return false;
            }

        }
        else
        {
            return false;
        }

    }
    public LocalDate convertToLocalDateObj(String date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate formattedDate = LocalDate.parse(date, formatter);
        return formattedDate;


    }






}
