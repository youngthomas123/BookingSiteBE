package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.BookingConflictException;
import fontys.s3.individual.bookingsite.business.exception.InvalidDatesException;
import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.exception.UserNotFoundException;
import fontys.s3.individual.bookingsite.business.useCase.CreateBookingUseCase;
import fontys.s3.individual.bookingsite.business.util.DateValidator;
import fontys.s3.individual.bookingsite.business.util.EmailHelper;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.request.CreateBookingRequest;
import fontys.s3.individual.bookingsite.domain.response.CreateBookingResponse;
import fontys.s3.individual.bookingsite.persistence.entity.BookingEntity;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.BookingRepository;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import fontys.s3.individual.bookingsite.persistence.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateBookingUseCaseImp implements CreateBookingUseCase
{
    private BookingRepository bookingRepository;
    private PropertyRepository propertyRepository;
    private UserRepository userRepository;
    private AccessToken accessToken;
    private DateValidator dateValidator;
    private EmailHelper emailHelper;
    @Override
    public CreateBookingResponse createBooking(CreateBookingRequest request)
    {
        //Get userEntity from token
        //check if property exists and avaliable to boook
        //create a bookingEntity
        //save the booking entity

        if(accessToken.hasRole("tenant"))
        {
           if(dateValidator.areDatesValid(request.getCheckIn(), request.getCheckOut()))
           {
               Optional<UserEntity> userEntity = userRepository.findById(accessToken.getUserId());

               if(userEntity.isPresent())
               {
                   UserEntity user = userEntity.get();
                   Optional<PropertyEntity> propertyEntity = propertyRepository.findById(request.getPropertyId());
                   LocalDate checkIn = dateValidator.convertToLocalDateObj(request.getCheckIn());
                   LocalDate checkOut = dateValidator.convertToLocalDateObj(request.getCheckOut());
                   boolean hasConflictingBookings = bookingRepository.hasConflictingBookings(request.getPropertyId(), checkIn,checkOut);

                   if(propertyEntity.isPresent() &&  propertyEntity.get().isEnlisted() &&!hasConflictingBookings)
                   {
                       BookingEntity bookingEntity = BookingEntity.builder()
                               .propertyEntity(propertyEntity.get())
                               .userEntity(user)
                               .checkIn(checkIn)
                               .checkOut(checkOut)
                               .build();

                       BookingEntity savedBookingEntity =  bookingRepository.save(bookingEntity);



                       CreateBookingResponse response = CreateBookingResponse.builder()
                               .bookingId(savedBookingEntity.getId())
                               .landlordName(propertyEntity.get().getUserEntity().getUsername())
                               .build();

                       try
                       {
                           emailHelper.sendBookingConfirmationEmail(user.getEmail(), bookingEntity, propertyEntity.get());
                           return response;
                       }
                       catch (MessagingException e)
                       {
                           throw new RuntimeException(e);
                       }
                   }
                   else
                   {
                       throw new BookingConflictException("Property cannot be booked or not found");
                   }


               }
               else
               {
                   throw new UserNotFoundException("The Tenant was not found in DB");
               }
           }
           else
           {
               throw new InvalidDatesException();
           }

        }
        else
        {
            throw new UnauthorizedDataAccessException("Invalid user role");
        }

    }
}
