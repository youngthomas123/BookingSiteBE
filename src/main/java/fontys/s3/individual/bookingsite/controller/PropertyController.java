package fontys.s3.individual.bookingsite.controller;

import fontys.s3.individual.bookingsite.business.useCase.GetPaginatedPropertiesUseCase;
import fontys.s3.individual.bookingsite.domain.request.GetPaginatedPropertiesRequest;
import fontys.s3.individual.bookingsite.domain.response.GetPaginatedPropertiesResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/properties")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class PropertyController
{
    private final GetPaginatedPropertiesUseCase getPaginatedPropertiesUseCase;

    @GetMapping
    public ResponseEntity<GetPaginatedPropertiesResponse> getPaginatedProperties(
            @RequestParam  String location,
            @RequestParam String checkIn,
            @RequestParam String checkOut,
            @RequestParam int page,
            @RequestParam int size) {

        GetPaginatedPropertiesRequest request = GetPaginatedPropertiesRequest.builder()
                .location(location)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .currentPage(page)
                .pageSize(size)
                .build();

        GetPaginatedPropertiesResponse response = getPaginatedPropertiesUseCase.getPaginatedProperties(request);

        return ResponseEntity.ok().body(response);
    }
}
