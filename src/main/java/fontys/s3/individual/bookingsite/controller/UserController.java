package fontys.s3.individual.bookingsite.controller;

import fontys.s3.individual.bookingsite.business.UserUseCase.GetAllUsersUseCase;
import fontys.s3.individual.bookingsite.domain.response.UserResponse.GetAllUsersResponse;
import fontys.s3.individual.bookingsite.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController
{
    private final GetAllUsersUseCase getAllUsersUseCase;

    @GetMapping
    public ResponseEntity<GetAllUsersResponse> getUsers()
    {
        return ResponseEntity.ok(getAllUsersUseCase.getAllUsers());

    }






}
