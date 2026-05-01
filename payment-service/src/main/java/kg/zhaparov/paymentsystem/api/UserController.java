package kg.zhaparov.paymentsystem.api;

import jakarta.validation.Valid;
import kg.zhaparov.paymentsystem.api.dto.CreateUserRequest;
import kg.zhaparov.paymentsystem.api.dto.UserDto;
import kg.zhaparov.paymentsystem.domain.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUserOrThrow(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
