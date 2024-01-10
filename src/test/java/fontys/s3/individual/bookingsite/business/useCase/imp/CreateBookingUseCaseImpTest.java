package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.TestConfig.TestEmailHelperConfig;
import fontys.s3.individual.bookingsite.business.exception.*;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
@Import(TestEmailHelperConfig.class)
class CreateBookingUseCaseImpTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessToken requestAccessToken;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private DateValidator dateValidator;

    @Mock
    private EmailHelper emailHelper;

    @InjectMocks
    private CreateBookingUseCaseImp createBookingUseCaseImp;

    @Test
    public void createBooking_InvalidRole_ThrowsException() {
        Mockito.when(requestAccessToken.hasRole("tenant")).thenReturn(false);

        CreateBookingRequest request = CreateBookingRequest.builder().build();

        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> {
            createBookingUseCaseImp.createBooking(request); // This should throw the exception
        });

    }

    @Test
    public void createBooking_ValidRoleButInvalidDates_ThrowsException() {
        Mockito.when(requestAccessToken.hasRole("tenant")).thenReturn(true);
        String checkin = "2020-1-10";
        String checkout = "2020 -1-3";
        CreateBookingRequest request = CreateBookingRequest.builder()
                .checkIn(checkin)
                .checkOut(checkout)
                .build();


        Mockito.when(dateValidator.areDatesValid(request.getCheckIn(), request.getCheckOut())).thenReturn(false);


        InvalidDatesException exception = assertThrows(InvalidDatesException.class, () -> {
            createBookingUseCaseImp.createBooking(request); // This should throw the exception
        });

    }

    @Test
    public void createBooking_ValidRoleAndDatesButUserDoesNotExistInDB_ThrowsException() {

        Mockito.when(requestAccessToken.hasRole("tenant")).thenReturn(true);

        String checkin = "2020-01-10";
        String checkout = "2020-01-12";
        CreateBookingRequest request = CreateBookingRequest.builder()
                .checkIn(checkin)
                .checkOut(checkout)
                .build();

        Mockito.when(dateValidator.areDatesValid(request.getCheckIn(), request.getCheckOut())).thenReturn(true);


        Mockito.when(requestAccessToken.getUserId()).thenReturn(2L);

        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());


        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            createBookingUseCaseImp.createBooking(request); // This should throw the exception
        });

    }

    @Test
    public void createBooking_ValidRoleAndDateButPropertyWasAlreadyBooked_ThrowsException() {
        Mockito.when(requestAccessToken.hasRole("tenant")).thenReturn(true);

        String checkin = "2020-01-10";
        String checkout = "2020-01-12";
        CreateBookingRequest request = CreateBookingRequest.builder()
                .checkIn(checkin)
                .checkOut(checkout)
                .propertyId(12L)
                .build();

        UserEntity tenant = UserEntity.builder()
                .id(2L)
                .username("thomas")
                .password("password")
                .build();

        Mockito.when(dateValidator.areDatesValid(request.getCheckIn(), request.getCheckOut())).thenReturn(true);

        Mockito.when(requestAccessToken.getUserId()).thenReturn(2L);

        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(tenant));

        LocalDate checkIn = dateValidator.convertToLocalDateObj(request.getCheckIn());
        LocalDate checkOut = dateValidator.convertToLocalDateObj(request.getCheckOut());

        Mockito.when(bookingRepository.hasConflictingBookings(request.getPropertyId(), checkIn, checkOut)).thenReturn(true);

        BookingConflictException exception = assertThrows(BookingConflictException.class, () -> {
            createBookingUseCaseImp.createBooking(request); // This should throw the exception
        });

    }

    @Test
    public void createBooking_ValidData_ReturnsResponse() throws MessagingException {
        Mockito.when(requestAccessToken.hasRole("tenant")).thenReturn(true);

        String checkin = "2020-01-10";
        String checkout = "2020-01-12";
        CreateBookingRequest request = CreateBookingRequest.builder()
                .checkIn(checkin)
                .checkOut(checkout)
                .propertyId(12L)
                .build();

        UserEntity tenant = UserEntity.builder()
                .id(2L)
                .username("jonny")
                .password("password")
                .email("jonny@gmail.com")
                .build();

        UserEntity landlord = UserEntity.builder()
                .id(31L)
                .username("thomas")
                .password("password")
                .build();

        PropertyEntity property = PropertyEntity.builder()
                .userEntity(landlord)
                .isEnlisted(true)
                .id(12L)
                .name("property1")
                .build();

        Mockito.when(dateValidator.areDatesValid(request.getCheckIn(), request.getCheckOut())).thenReturn(true);

        Mockito.when(requestAccessToken.getUserId()).thenReturn(2L);

        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(tenant));

        Mockito.when(propertyRepository.findById(request.getPropertyId())).thenReturn(Optional.ofNullable(property));
        LocalDate checkIn = dateValidator.convertToLocalDateObj(request.getCheckIn());
        LocalDate checkOut = dateValidator.convertToLocalDateObj(request.getCheckOut());


        Mockito.when(bookingRepository.hasConflictingBookings(request.getPropertyId(), checkIn, checkOut)).thenReturn(false);

        BookingEntity savedBookingEntity = BookingEntity.builder()
                .id(1L)
                .build();

        Mockito.when(bookingRepository.save(any())).thenReturn(savedBookingEntity);


        CreateBookingResponse expectedResponse = CreateBookingResponse.builder()
                .landlordName(landlord.getUsername())
                .build();

        CreateBookingResponse response = createBookingUseCaseImp.createBooking(request);
        assertNotNull(response);
        assertEquals(expectedResponse.getLandlordName(), response.getLandlordName());
        Mockito.verify(emailHelper).sendBookingConfirmationEmail(eq(tenant.getEmail()), any(BookingEntity.class), any(PropertyEntity.class));

    }

    @Test
    public void createBooking_ValidDataButErrorSendingEmail_ThrowsEmailException() throws MessagingException {
        Mockito.when(requestAccessToken.hasRole("tenant")).thenReturn(true);

        // ... (initialize other objects and mock behaviors)
        Mockito.when(requestAccessToken.hasRole("tenant")).thenReturn(true);

        String checkin = "2020-01-10";
        String checkout = "2020-01-12";
        CreateBookingRequest request = CreateBookingRequest.builder()
                .checkIn(checkin)
                .checkOut(checkout)
                .propertyId(12L)
                .build();

        UserEntity tenant = UserEntity.builder()
                .id(2L)
                .username("jonny")
                .password("password")
                .email("jonny@gmail.com")
                .build();

        UserEntity landlord = UserEntity.builder()
                .id(31L)
                .username("thomas")
                .password("password")
                .build();

        PropertyEntity property = PropertyEntity.builder()
                .userEntity(landlord)
                .isEnlisted(true)
                .id(12L)
                .name("property1")
                .build();

        Mockito.when(dateValidator.areDatesValid(request.getCheckIn(), request.getCheckOut())).thenReturn(true);

        Mockito.when(requestAccessToken.getUserId()).thenReturn(2L);

        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(tenant));

        Mockito.when(propertyRepository.findById(request.getPropertyId())).thenReturn(Optional.ofNullable(property));
        LocalDate checkIn = dateValidator.convertToLocalDateObj(request.getCheckIn());
        LocalDate checkOut = dateValidator.convertToLocalDateObj(request.getCheckOut());


        Mockito.when(bookingRepository.hasConflictingBookings(request.getPropertyId(), checkIn, checkOut)).thenReturn(false);

        BookingEntity savedBookingEntity = BookingEntity.builder()
                .id(1L)
                .build();

        Mockito.when(bookingRepository.save(any())).thenReturn(savedBookingEntity);


        // Mocking the behavior when sending email throws an exception
        Mockito.doThrow(new MessagingException("Email sending failed"))
                .when(emailHelper)
                .sendBookingConfirmationEmail(
                        eq(tenant.getEmail()),
                        any(BookingEntity.class),
                        any(PropertyEntity.class)
                );

        // Verify that the EmailException is thrown
        assertThrows(EmailException.class, () -> {
            createBookingUseCaseImp.createBooking(request);
        });


    }




}
