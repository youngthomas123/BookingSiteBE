package fontys.s3.individual.bookingsite.controller;

import fontys.s3.individual.bookingsite.business.useCase.CreateUserUseCase;
import fontys.s3.individual.bookingsite.business.useCase.GetAllUsersUseCase;
import fontys.s3.individual.bookingsite.business.useCase.GetUserByIdUseCase;
import fontys.s3.individual.bookingsite.domain.dto.UserDetailsDTO;
import fontys.s3.individual.bookingsite.domain.dto.UserSignUpDTO;
import fontys.s3.individual.bookingsite.domain.request.CreateUserRequest;
import fontys.s3.individual.bookingsite.domain.response.CreateUserResponse;
import fontys.s3.individual.bookingsite.domain.response.GetAllUsersResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:5173/")
public class UserController
{
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final CreateUserUseCase createUserUseCase;

    @GetMapping
    public ResponseEntity<GetAllUsersResponse> getUsers()
    {
        return ResponseEntity.ok(getAllUsersUseCase.getAllUsers());

    }


    @GetMapping("{id}")
    public ResponseEntity<UserDetailsDTO> getUser(@PathVariable(value = "id") final long id)
    {
         Optional<UserDetailsDTO> userOptional = getUserByIdUseCase.getUserById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userOptional.get());
    }

    @PostMapping()
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = createUserUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }






}
