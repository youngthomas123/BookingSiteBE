package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.BookingConflictException;
import fontys.s3.individual.bookingsite.business.exception.InvalidDatesException;
import fontys.s3.individual.bookingsite.business.exception.ItemNotFoundException;
import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.util.DateValidator;
import fontys.s3.individual.bookingsite.domain.response.GetPropertyByIdResponse;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyPictureEntity;
import fontys.s3.individual.bookingsite.persistence.repository.BookingRepository;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyPictureRepository;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetPropertyByIdUseCaseImpTest
{
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private PropertyPictureRepository propertyPictureRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private DateValidator dateValidator;

    @InjectMocks
    private GetPropertyByIdUseCaseImp getPropertyByIdUseCaseImp;

    @Test
    void getPropertyById_ValidInput_ReturnsResponse()
    {
        // Arrange
        String checkIn = "2020-01-01";
        String checkOut = "2020-01-02";
        when(dateValidator.areDatesValid(checkIn, checkOut)).thenReturn(true);

        PropertyEntity propertyEntity = PropertyEntity.builder()
                .id(1L)
                .name("property")
                .isEnlisted(true)
                .build();

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(propertyEntity));

        List<PropertyPictureEntity> propertyPictureEntityList = new ArrayList<>();

        PropertyPictureEntity propertyPictureEntity = PropertyPictureEntity.builder()
                .id(1L)
                .photo("photo")
                .build();

        propertyPictureEntityList.add(propertyPictureEntity);

        when(propertyPictureRepository.findByPropertyEntityId(propertyEntity.getId())).thenReturn(propertyPictureEntityList);

        when(dateValidator.convertToLocalDateObj(any())).thenReturn(LocalDate.now());
        when(bookingRepository.hasConflictingBookings(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(false);

        // Act
        GetPropertyByIdResponse response = getPropertyByIdUseCaseImp.getPropertyById(1L, checkIn, checkOut);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getPropertyPageDTO());
        assertEquals(1L, response.getPropertyPageDTO().getPropertyId());
        assertEquals("property", response.getPropertyPageDTO().getName());
        assertEquals("photo", response.getPropertyPageDTO().getOtherPhotos().get(0));
        assertFalse(response.getPropertyPageDTO().isHasConflictingBookings());

        // Verify that the necessary methods were called
        verify(propertyRepository, times(1)).findById(anyLong());
        verify(propertyPictureRepository, times(1)).findByPropertyEntityId(anyLong());
        verify(bookingRepository, times(1)).hasConflictingBookings(anyLong(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getPropertyById_InvalidDates_ReturnsResponse()
    {
        String checkIn = "2020-1-01";
        String checkOut = "2020-00-02";
        when(dateValidator.areDatesValid(checkIn, checkOut)).thenReturn(false);

        assertThrows(InvalidDatesException.class, () ->
                getPropertyByIdUseCaseImp.getPropertyById(2L, checkIn, checkOut));

    }

    @Test
    void getPropertyById_PropertyNotInDB_ThrowsException()
    {
        String checkIn = "2020-01-01";
        String checkOut = "2020-01-02";
        when(dateValidator.areDatesValid(checkIn, checkOut)).thenReturn(true);

        when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () ->
                getPropertyByIdUseCaseImp.getPropertyById(1L, checkIn, checkOut));

    }

    @Test
    void getPropertyById_NotEnlistedProperty_ThrowsException()
    {
        String checkIn = "2020-01-01";
        String checkOut = "2020-01-02";
        when(dateValidator.areDatesValid(checkIn, checkOut)).thenReturn(true);

        PropertyEntity propertyEntity = PropertyEntity.builder()
                .id(1L)
                .name("property")
                .isEnlisted(false)
                .build();

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(propertyEntity));

        assertThrows(UnauthorizedDataAccessException.class, () ->
                getPropertyByIdUseCaseImp.getPropertyById(1L, checkIn, checkOut));


    }



}