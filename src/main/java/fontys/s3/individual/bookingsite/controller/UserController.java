package fontys.s3.individual.bookingsite.controller;

import fontys.s3.individual.bookingsite.business.useCase.*;
import fontys.s3.individual.bookingsite.domain.dto.UserDetailsDTO;
import fontys.s3.individual.bookingsite.domain.request.CreateUserRequest;
import fontys.s3.individual.bookingsite.domain.request.UpdateUserRequest;
import fontys.s3.individual.bookingsite.domain.response.CreateUserResponse;
import fontys.s3.individual.bookingsite.domain.response.GetAllUsersResponse;
import fontys.s3.individual.bookingsite.domain.response.UpdateUserResponse;
import fontys.s3.individual.bookingsite.domain.response.UpdateUserStatusResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor

public class UserController
{
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserByIdUseCase updateUserByIdUseCase;
    private final UpdateUserStatusUseCase updateUserStatusUseCase;

    @RolesAllowed({"admin",})
    @GetMapping
    public ResponseEntity<GetAllUsersResponse> getUsers()
    {
        return ResponseEntity.ok(getAllUsersUseCase.getAllUsers());

    }


    @GetMapping("{id}")
    public ResponseEntity<UserDetailsDTO> getUserById(@PathVariable(value = "id") final long id)
    {
        Optional<UserDetailsDTO> userOptional = getUserByIdUseCase.getUserById(id);
        if (userOptional.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userOptional.get());
    }


    @PostMapping()
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request)
    {
        CreateUserResponse response = createUserUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }





    @PutMapping(value = "{id}", consumes = "multipart/form-data")
    public ResponseEntity<UpdateUserResponse> updateUserById(
            @PathVariable("id") long id,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("email") String email,
            @RequestParam("bio") String bio,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        UpdateUserRequest request = UpdateUserRequest.builder()
                .phoneNumber(phoneNumber)
                .email(email)
                .bio(bio)
                .build();

        if (imageFile != null)
        {
            request.setProfilePic(imageFile);
        }

        UpdateUserResponse response = updateUserByIdUseCase.updateUser(request, id);



        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @RolesAllowed({"admin"})
    @PutMapping("/timeout/{userId}")
    public ResponseEntity<UpdateUserStatusResponse>updateUserStatus(
            @PathVariable("userId") long userId,
            @RequestParam ("status") String status)
    {
        UpdateUserStatusResponse response = updateUserStatusUseCase.updateUserStatus(userId, status);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



}
