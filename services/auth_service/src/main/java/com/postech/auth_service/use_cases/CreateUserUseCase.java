package com.postech.auth_service.use_cases;

import com.postech.auth_service.dto.CreateUserDto;
import com.postech.auth_service.dto.UserDto;
import com.postech.auth_service.entity.User;
import com.postech.auth_service.repository.UserRepository;
import com.postech.auth_service.use_cases.base.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateUserUseCase implements UseCase<CreateUserDto, UserDto> {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto execute(CreateUserDto params) {
        var user = new User();
        user.setUsername(params.username());
        user.setPassword(this.passwordEncoder.encode(params.password()));
        user.setName(params.name());
        user.setEmail(params.email());
        user.setRole(params.role());

        var savedUser = this.repository.save(user);

        return new UserDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
}
