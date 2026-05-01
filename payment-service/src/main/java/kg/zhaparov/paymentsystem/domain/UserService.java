package kg.zhaparov.paymentsystem.domain;

import kg.zhaparov.paymentsystem.api.dto.CreateUserRequest;
import kg.zhaparov.paymentsystem.api.dto.UserDto;
import kg.zhaparov.paymentsystem.domain.db.UserEntity;
import kg.zhaparov.paymentsystem.domain.db.UserRepository;
import kg.zhaparov.paymentsystem.mapper.PaymentMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PaymentMapper mapper;

    public UserService(UserRepository userRepository, PaymentMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional
    public UserDto createUser(CreateUserRequest request) {
        boolean exists = userRepository.existsByEmailIgnoreCase(request.email());
        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User already exists: " + request.email()
            );
        }
        UserEntity user = new UserEntity(request.email());
        UserEntity saved = userRepository.save(user);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserDto getUserOrThrow(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAllWithUsers().stream()
                .map(this::toResponse)
                .toList();
    }

    private UserDto toResponse(UserEntity user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                mapper.mapListToDto(user.getPayments())
        );
    }
}
