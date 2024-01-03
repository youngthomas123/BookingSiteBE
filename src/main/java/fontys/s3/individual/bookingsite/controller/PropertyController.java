package fontys.s3.individual.bookingsite.controller;

import fontys.s3.individual.bookingsite.business.useCase.CreatePropertyEnlistingUseCase;
import fontys.s3.individual.bookingsite.business.useCase.GetLandlordPropertiesByIdUseCase;
import fontys.s3.individual.bookingsite.business.useCase.GetPaginatedPropertiesUseCase;
import fontys.s3.individual.bookingsite.business.useCase.GetPropertyByIdUseCase;
import fontys.s3.individual.bookingsite.domain.request.CreatePropertyEnlistingRequest;
import fontys.s3.individual.bookingsite.domain.request.GetPaginatedPropertiesRequest;
import fontys.s3.individual.bookingsite.domain.response.CreatePropertyEnlistingResponse;
import fontys.s3.individual.bookingsite.domain.response.GetLandlordPropertiesByIdResponse;
import fontys.s3.individual.bookingsite.domain.response.GetPaginatedPropertiesResponse;
import fontys.s3.individual.bookingsite.domain.response.GetPropertyByIdResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/properties")
@AllArgsConstructor

public class  PropertyController
{
    private final GetPaginatedPropertiesUseCase getPaginatedPropertiesUseCase;
    private final CreatePropertyEnlistingUseCase createPropertyEnlistingUseCase;
    private final GetPropertyByIdUseCase getPropertyByIdUseCase;
    private final GetLandlordPropertiesByIdUseCase getLandlordPropertiesByIdUseCase;
    


    @RolesAllowed({"tenant"})
    @GetMapping
    public ResponseEntity<GetPaginatedPropertiesResponse> getPaginatedProperties(
            @RequestParam  String location,
            @RequestParam String checkIn,
            @RequestParam String checkOut,
            @RequestParam int page,
            @RequestParam int size)
    {

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


    @RolesAllowed({"landlord"})
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<CreatePropertyEnlistingResponse>createPropertyEnlisting(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("pricePerNight") String pricePerNight,
            @RequestPart(value = "mainPhoto") MultipartFile mainPhoto,
            @RequestPart(value = "otherPhotos")List<MultipartFile> otherPhotos
            )
    {

        CreatePropertyEnlistingRequest request = CreatePropertyEnlistingRequest.builder()
                .propertyName(name)
                .description(description)
                .location(location)
                .pricePerNight(Double.parseDouble(pricePerNight))
                .mainPicture(mainPhoto)
                .otherPhotos(otherPhotos)
                .build();
       CreatePropertyEnlistingResponse response = createPropertyEnlistingUseCase.createPropertyEnlisting(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


    @GetMapping("{id}")
    public ResponseEntity<GetPropertyByIdResponse>getPropertyById(@PathVariable(value = "id") final long id)
    {
        GetPropertyByIdResponse response = getPropertyByIdUseCase.getPropertyById(id);
        return ResponseEntity.ok().body(response);
    }

    @RolesAllowed({"landlord"})
    @GetMapping("/landlord/{landlordId}")
    public ResponseEntity<GetLandlordPropertiesByIdResponse>getLandlordPropertiesById(@PathVariable(value = "landlordId") final long landlordId)
    {
        GetLandlordPropertiesByIdResponse response = getLandlordPropertiesByIdUseCase.getProperties(landlordId);
        return ResponseEntity.ok().body(response);
    }





}
