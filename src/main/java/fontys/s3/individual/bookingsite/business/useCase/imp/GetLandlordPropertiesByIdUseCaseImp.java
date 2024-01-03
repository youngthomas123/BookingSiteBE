package fontys.s3.individual.bookingsite.business.useCase.imp;

import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.useCase.GetLandlordPropertiesByIdUseCase;
import fontys.s3.individual.bookingsite.configuration.security.token.AccessToken;
import fontys.s3.individual.bookingsite.domain.dto.DisplayLandlordPropertyDTO;
import fontys.s3.individual.bookingsite.domain.dto.UserDetailsDTO;
import fontys.s3.individual.bookingsite.domain.response.GetLandlordPropertiesByIdResponse;
import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;
import fontys.s3.individual.bookingsite.persistence.repository.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GetLandlordPropertiesByIdUseCaseImp implements GetLandlordPropertiesByIdUseCase
{
    private PropertyRepository propertyRepository;
    private AccessToken requestAccessToken;

    @Override
    public GetLandlordPropertiesByIdResponse getProperties(long id)
    {
        //check if user is a landlord
        //check if the id passed in is equal to the landlord id in the access token
        if(requestAccessToken.hasRole("landlord") && requestAccessToken.getUserId() == id)
        {
            //get the properties from db
            // create list of dtos
            //create and return response
            List<PropertyEntity> properties = propertyRepository.findByUserEntityId(id);
            List<DisplayLandlordPropertyDTO> propertyDtos = properties.stream()
                    .map(property -> DisplayLandlordPropertyDTO.builder()
                            .propertyName(property.getName())
                            .mainPhoto(property.getMainPhoto())
                            .build())
                    .toList();

            GetLandlordPropertiesByIdResponse response = GetLandlordPropertiesByIdResponse.builder()
                    .properties(propertyDtos)
                    .build();

            return response;
        }
        else
        {
            throw new UnauthorizedDataAccessException("Role or id dont match");
        }

    }
}
