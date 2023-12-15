package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.InvalidDatesException;
import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.util.DateValidator;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.dto.PropertyHomePageDTO;
import fontys.s3.individual.bookingsite.domain.request.GetPaginatedPropertiesRequest;
import fontys.s3.individual.bookingsite.domain.response.GetPaginatedPropertiesResponse;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPaginatedPropertiesUseCaseImpTest
{
    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private AccessToken requestAccessToken;

    @Mock
    private DateValidator dateValidator;

    @InjectMocks
    private GetPaginatedPropertiesUseCaseImp getPaginatedPropertiesUseCaseImp;

    @Test
    public void getPaginatedProperties_HasValidRoleAndValidDates_ReturnsResponse()
    {
        // Arrange
        GetPaginatedPropertiesRequest request = GetPaginatedPropertiesRequest.builder()
                .location("USA")
                .checkIn("2023-11-29")
                .checkOut("2023-11-30")
                .pageSize(6)
                .currentPage(0)
                .build();



        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("thomas")
                .password("123")
                .build();


        PropertyEntity property1 = PropertyEntity.builder()
                .id(1L)
                .description("Sample description 1")
                .name("property1")
                .location("randomLocation1")
                .pricePerNight(21)
                .mainPhoto("https://t3.ftcdn.net/jpg/02/48/42/64/360_F_248426448_NVKLywWqArG2ADUxDq6QprtIzsF82dMF.jpg")
                .userEntity(userEntity)
                .build();

        PropertyEntity property2 = PropertyEntity.builder()
                .id(2L)
                .description("Sample description 2")
                .name("property2")
                .location("randomLocation2")
                .pricePerNight(23)
                .mainPhoto("https://t3.ftcdn.net/jpg/02/48/42/64/360_F_248426448_NVKLywWqArG2ADUxDq6QprtIzsF82dMF.jpg")
                .userEntity(userEntity)
                .build();

        PropertyEntity property3 = PropertyEntity.builder()
                .id(3L)
                .description("Sample description 3")
                .name("property3")
                .location("randomLocation3")
                .pricePerNight(21)
                .mainPhoto("https://t3.ftcdn.net/jpg/02/48/42/64/360_F_248426448_NVKLywWqArG2ADUxDq6QprtIzsF82dMF.jpg")
                .userEntity(userEntity)
                .build();

        PropertyEntity property4 = PropertyEntity.builder()
                .id(4L)
                .description("Sample description 4")
                .name("property4")
                .location("randomLocation4")
                .pricePerNight(23)
                .mainPhoto("https://t3.ftcdn.net/jpg/02/48/42/64/360_F_248426448_NVKLywWqArG2ADUxDq6QprtIzsF82dMF.jpg")
                .userEntity(userEntity)
                .build();

        PropertyEntity property5 = PropertyEntity.builder()
                .id(5L)
                .description("Sample description 5")
                .name("property5")
                .location("randomLocation5")
                .pricePerNight(21)
                .mainPhoto("https://t3.ftcdn.net/jpg/02/48/42/64/360_F_248426448_NVKLywWqArG2ADUxDq6QprtIzsF82dMF.jpg")
                .userEntity(userEntity)
                .build();

        PropertyEntity property6 = PropertyEntity.builder()
                .id(6L)
                .description("Sample description 6")
                .name("property6")
                .location("randomLocation6")
                .pricePerNight(23)
                .mainPhoto("https://t3.ftcdn.net/jpg/02/48/42/64/360_F_248426448_NVKLywWqArG2ADUxDq6QprtIzsF82dMF.jpg")
                .userEntity(userEntity)
                .build();

        List<PropertyEntity> propertyEntityList = new ArrayList<>();

        propertyEntityList.add(property1);
        propertyEntityList.add(property2);
        propertyEntityList.add(property3);
        propertyEntityList.add(property4);
        propertyEntityList.add(property5);
        propertyEntityList.add(property6);



        Page<PropertyEntity> propertyEntities = new PageImpl<>(propertyEntityList);

        PageRequest pageRequest = PageRequest.of(request.getCurrentPage(), request.getPageSize());

        when(dateValidator.areDatesValid(request.getCheckIn(), request.getCheckOut())).thenReturn(true);
        when(requestAccessToken.hasRole("tenant")).thenReturn(true);
        when(propertyRepository.findPaginatedByLocationAndAvailability(request.getLocation(), request.getCheckIn(), request.getCheckOut(), pageRequest)).thenReturn(propertyEntities);

        // act
        GetPaginatedPropertiesResponse actualResponse = getPaginatedPropertiesUseCaseImp.getPaginatedProperties(request);

        // assert
        List<PropertyHomePageDTO> propertyDTOs = propertyEntityList.stream()
                .map(property -> PropertyHomePageDTO.builder()
                        .propertyId(property.getId())
                        .description(property.getDescription())
                        .landlordId(property.getUserEntity().getId())
                        .priceForNight(property.getPricePerNight())
                        .name(property.getName())
                        .mainPhoto(property.getMainPhoto())
                        .build())
                .collect(Collectors.toList());





        GetPaginatedPropertiesResponse expectedResponse = GetPaginatedPropertiesResponse.builder()
                .totalCount(6L)
                .Properties(propertyDTOs)
                .build();

        assertNotNull(actualResponse);


        assertEquals(expectedResponse, actualResponse);


    }

    @Test
    public void getPaginatedProperties_HasValidRoleButInvalidDates_ThrowsException()
    {
        // Arrange
        GetPaginatedPropertiesRequest request = GetPaginatedPropertiesRequest.builder()
                .location("USA")
                .checkIn("2023-11-29")
                .checkOut("2023-11-12")
                .pageSize(6)
                .currentPage(0)
                .build();

        when(dateValidator.areDatesValid(request.getCheckIn(), request.getCheckOut())).thenReturn(false);
        when(requestAccessToken.hasRole("tenant")).thenReturn(true);

        InvalidDatesException exception = assertThrows(InvalidDatesException.class, () -> {
            getPaginatedPropertiesUseCaseImp.getPaginatedProperties(request); // This should throw the exception
        });

    }


    @Test
    public void getPaginatedProperties_HasInvalidRole_ThrowsException()
    {
        //arrange
        GetPaginatedPropertiesRequest request = GetPaginatedPropertiesRequest.builder()
                .location("USA")
                .checkIn("2023-11-29")
                .checkOut("2023-11-30")
                .pageSize(3)
                .currentPage(0)
                .build();

        //act and assert
        when(requestAccessToken.hasRole("tenant")).thenReturn(false);
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> {
            getPaginatedPropertiesUseCaseImp.getPaginatedProperties(request); // This should throw the exception
        });




    }


}