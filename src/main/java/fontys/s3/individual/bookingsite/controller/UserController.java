package fontys.s3.individual.bookingsite.controller;

import fontys.s3.individual.bookingsite.business.UserUseCase.CreateUserUseCase;
import fontys.s3.individual.bookingsite.business.UserUseCase.GetAllUsersUseCase;
import fontys.s3.individual.bookingsite.business.UserUseCase.GetUserByIdUseCase;
import fontys.s3.individual.bookingsite.business.UserUseCaseImp.CreateUserUseCaseImp;
import fontys.s3.individual.bookingsite.domain.model.User;
import fontys.s3.individual.bookingsite.domain.request.UserRequest.CreateUserRequest;
import fontys.s3.individual.bookingsite.domain.response.UserResponse.CreateUserResponse;
import fontys.s3.individual.bookingsite.domain.response.UserResponse.GetAllUsersResponse;
import fontys.s3.individual.bookingsite.persistence.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
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
    public ResponseEntity<User> getUser(@PathVariable(value = "id") final long id) {
        final Optional<User> userOptional = getUserByIdUseCase.getUserById(id);
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
