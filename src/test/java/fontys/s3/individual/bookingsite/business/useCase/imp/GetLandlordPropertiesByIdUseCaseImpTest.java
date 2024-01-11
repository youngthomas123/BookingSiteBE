package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyPictureEntity;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import fontys.s3.individual.bookingsite.persistence.repository.BookingRepository;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class GetLandlordPropertiesByIdUseCaseImpTest
{
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private AccessToken requestAccessToken;
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private GetLandlordPropertiesByIdUseCaseImp getLandlordPropertiesByIdUseCaseImp;

    @Test
    void getProperties_ValidInput_ReturnsResponse()
    {
        long landlordId = 1L;
        when(requestAccessToken.hasRole("landlord")).thenReturn(true);
        when(requestAccessToken.getUserId()).thenReturn(landlordId);
        List<PropertyEntity> propertyEntities = new ArrayList<>();
        PropertyEntity propertyEntity1 = PropertyEntity.builder()
                .id(1L)
                .name("name1")
                .mainPhoto("mainPhoto1")
                .isEnlisted(true)
                .build();
        propertyEntities.add(propertyEntity1);


        when(bookingRepository.existsBookingsAfterTodayForProperty(1L, LocalDate.now())).thenReturn(true);

        when(propertyRepository.findByUserEntityId(landlordId)).thenReturn(propertyEntities);


        //assert
        getLandlordPropertiesByIdUseCaseImp.getProperties(landlordId);

        verify(bookingRepository, times(1)).existsBookingsAfterTodayForProperty(1L, LocalDate.now());


    }

    @Test
    void getProperties_InvalidRole_ReturnsResponse()
    {
        when(requestAccessToken.hasRole("landlord")).thenReturn(false);
        assertThrows(UnauthorizedDataAccessException.class, () ->
                getLandlordPropertiesByIdUseCaseImp.getProperties(2L));

    }

}