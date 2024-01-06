package fontys.s3.individual.bookingsite.controller;

import fontys.s3.individual.bookingsite.business.useCase.CreateBookingUseCase;
import fontys.s3.individual.bookingsite.domain.request.CreateBookingRequest;
import fontys.s3.individual.bookingsite.domain.response.CreateBookingResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController
{
    private final CreateBookingUseCase createBookingUseCase;

    @RolesAllowed({"tenant"})
    @PostMapping()
    public ResponseEntity<CreateBookingResponse> createBooking(
            @RequestParam("propertyId") long propertyId,
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut
    )
    {
        CreateBookingRequest request = CreateBookingRequest.builder()
                .propertyId(propertyId)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .build();

        CreateBookingResponse response = createBookingUseCase.createBooking(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



}
