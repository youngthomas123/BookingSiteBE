package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.useCase.GetPaginatedPropertiesUseCase;
import fontys.s3.individual.bookingsite.domain.dto.PropertyHomePageDTO;
import fontys.s3.individual.bookingsite.domain.dto.UserDetailsDTO;
import fontys.s3.individual.bookingsite.domain.request.GetPaginatedPropertiesRequest;
import fontys.s3.individual.bookingsite.domain.response.GetPaginatedPropertiesResponse;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class GetPaginatedPropertiesUseCaseImp implements GetPaginatedPropertiesUseCase
{

    private final PropertyRepository propertyRepository;


    @Override
    public GetPaginatedPropertiesResponse getPaginatedProperties(GetPaginatedPropertiesRequest request)
    {

        PageRequest pageRequest = PageRequest.of(request.getCurrentPage(), request.getPageSize());

        Page<PropertyEntity> propertyEntities = propertyRepository.findPaginatedByLocationAndAvailability(
                request.getLocation(), request.getCheckIn(), request.getCheckOut(), pageRequest);



        // Convert Page<> to List<>
        List<PropertyEntity> propertiesList = propertyEntities.getContent();

        // create dtos



        List<PropertyHomePageDTO> propertyDTOs = propertiesList.stream()
                .map(property -> PropertyHomePageDTO.builder()
                        .propertyId(property.getId())
                        .description(property.getDescription())
                        .landlordId(property.getUserEntity().getId())
                        .priceForNight(property.getPricePerNight())
                        .name(property.getName())
                        .mainPhoto(property.getMainPhoto())
                        .build())
                .collect(Collectors.toList());



        GetPaginatedPropertiesResponse response = GetPaginatedPropertiesResponse.builder()
                .totalCount(propertyEntities.getTotalElements())
                .Properties(propertyDTOs)
                .build();
        return response;
    }

}
