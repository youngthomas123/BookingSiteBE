package fontys.s3.individual.bookingsite.business.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class ImageStorageHelper
{
    private final WebClient webClient;

    public String saveProfilePic(MultipartFile file)
    {
        // Prepare the multipart request with the image file
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", file.getResource());

        // Send a POST request to the image server with the MultipartFile as multipart form data
        String imageUrl = webClient.post()
                .uri("http://localhost:3000/upload/ProfilePic") // Replace with the image server's upload endpoint
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Blocking call to retrieve the response (consider asynchronous handling)

        return imageUrl;
    }
    public void deleteProfilePic(String oldUrl)
    {
        String imageUrl = "{\"imageUrl\": \"" + oldUrl + "\"}";


        webClient.post()
                .uri("http://localhost:3000/delete/ProfilePic")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(imageUrl)
                .retrieve()
                .toBodilessEntity()
                .block(); // Blocking call to perform deletion (consider asynchronous handling)
    }

    public String saveMainPropertyPic(MultipartFile file)
    {
        // Prepare the multipart request with the image file
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", file.getResource());


        // Send a POST request to the image server with the MultipartFile as multipart form data
        String imageUrl = webClient.post()
                .uri("http://localhost:3000/upload/MainPropertyPic") // Replace with the image server's upload endpoint
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Blocking call to retrieve the response (consider asynchronous handling)

        return imageUrl;

    }

    public List<String> saveOtherPropertyPhotos(List<MultipartFile> files)
    {
        // Prepare the multipart request with the image files
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (MultipartFile file : files) {
            body.add("image", file.getResource());
        }

        // Send a POST request to the image server with the MultipartFile as multipart form data
        List<String> imageUrls = webClient.post()
                .uri("http://localhost:3000/upload/PropertyPhoto") // Replace with the image server's upload endpoint
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .bodyToFlux(String.class) // Assuming the server returns a Flux of String URLs
                .collectList() // Collect the Flux into a List
                .block(); // Blocking call to retrieve the response (consider asynchronous handling)

        // Extract image URLs from the JSON response
        if (imageUrls != null && imageUrls.size() == 1) {
            String jsonResponse = imageUrls.get(0);
            JsonObject jsonObject = new Gson().fromJson(jsonResponse, JsonObject.class);
            JsonArray imageURLs = jsonObject.getAsJsonArray("imageURLs");

            List<String> urls = new ArrayList<>();
            for (JsonElement element : imageURLs) {
                urls.add(element.getAsString());
            }

            return urls;
        } else {
            // Handle null or unexpected response
            return Collections.emptyList(); // or throw an exception
        }
    }




}
