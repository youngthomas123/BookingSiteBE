package fontys.s3.individual.bookingsite.controller;

import fontys.s3.individual.bookingsite.business.exception.UnauthorizedDataAccessException;
import fontys.s3.individual.bookingsite.business.useCase.GetPaginatedPropertiesUseCase;
import fontys.s3.individual.bookingsite.domain.dto.PropertyHomePageDTO;
import fontys.s3.individual.bookingsite.domain.request.GetPaginatedPropertiesRequest;
import fontys.s3.individual.bookingsite.domain.response.GetPaginatedPropertiesResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc


class PropertyControllerIntegrationTest
{
    @Autowired
    private MockMvc mockMvc;



    @MockBean
    private GetPaginatedPropertiesUseCase getPaginatedPropertiesUseCase;


    @Test
    @WithMockUser(username = "thomas", roles = {"tenant"})
    public void getPaginatedProperties_ValidRouteParamsAndRole_ShouldReturnStatus200() throws Exception
    {
        // Mocked request parameters
        String location = "USA";
        String checkIn = "2024-11-29";
        String checkOut = "2024-11-30";
        int page = 0;
        int size = 2;

        GetPaginatedPropertiesRequest request = GetPaginatedPropertiesRequest.builder()
                .location(location)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .currentPage(page)
                .pageSize(size)
                .build();

        PropertyHomePageDTO dto1 = PropertyHomePageDTO.builder()
                .propertyId(1L)
                .description("description1")
                .name("name1")
                .mainPhoto("https://t3.ftcdn.net/jpg/02/48/42/64/360_F_248426448_NVKLywWqArG2ADUxDq6QprtIzsF82dMF.jpg")
                .priceForNight(21.0)
                .landlordId(1L)
                .build();

        PropertyHomePageDTO dto2 = PropertyHomePageDTO.builder()
                .propertyId(2L)
                .description("description2")
                .name("name2")
                .mainPhoto("https://t3.ftcdn.net/jpg/02/48/42/64/360_F_248426448_NVKLywWqArG2ADUxDq6QprtIzsF82dMF.jpg")
                .priceForNight(21.0)
                .landlordId(1L)
                .build();

        List<PropertyHomePageDTO> propertyDtos = new ArrayList<>();

        propertyDtos.add(dto1);
        propertyDtos.add(dto2);


        GetPaginatedPropertiesResponse response = GetPaginatedPropertiesResponse.builder()
                .totalCount(10L)
                .Properties(propertyDtos)
                .build();

        when(getPaginatedPropertiesUseCase.getPaginatedProperties(request)).thenReturn(response);

        mockMvc.perform(get("/properties")
                        .param("location", location)
                        .param("checkIn", checkIn)
                        .param("checkOut", checkOut)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                            {
                                 "totalCount": 10,
                                  "properties": [
                                         {
                                             "propertyId": 1,
                                             "description": "description1",
                                             "name": "name1",
                                             "priceForNight": 21.0,
                                             "landlordId": 1,
                                             "mainPhoto": "https://t3.ftcdn.net/jpg/02/48/42/64/360_F_248426448_NVKLywWqArG2ADUxDq6QprtIzsF82dMF.jpg"
                                         },
                                         {
                                             "propertyId": 2,
                                             "description": "description2",
                                             "name": "name2",
                                             "priceForNight": 21.0,
                                             "landlordId": 1,
                                             "mainPhoto": "https://t3.ftcdn.net/jpg/02/48/42/64/360_F_248426448_NVKLywWqArG2ADUxDq6QprtIzsF82dMF.jpg"
                                         }
                                     ]
                            }
                        """));

    }

    @Test
    @WithMockUser(username = "thomas", roles = {"tenant"})
    public void getPaginatedProperties_MissingParams() throws Exception
    {
        // Mocked request parameters (missing page and size)
        String location = "USA";
        String checkIn = "2024-11-29";
        String checkOut = "2024-11-30";

        mockMvc.perform(get("/properties")
                        .param("location", location)
                        .param("checkIn", checkIn)
                        .param("checkOut", checkOut))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "thomas", roles = {"landlord"})
    public void getPaginatedProperties_HasValidRouteParamButInvalidRole_ThrowsException() throws Exception
    {
        // Mocked request parameters
        String location = "USA";
        String checkIn = "2024-11-29";
        String checkOut = "2024-11-30";
        int page = 0;
        int size = 2;

        GetPaginatedPropertiesRequest request = GetPaginatedPropertiesRequest.builder()
                .location(location)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .currentPage(page)
                .pageSize(size)
                .build();

        mockMvc.perform(get("/properties")
                        .param("location", location)
                        .param("checkIn", checkIn)
                        .param("checkOut", checkOut)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isForbidden()); // Check for 403 Forbidden status

    }

}