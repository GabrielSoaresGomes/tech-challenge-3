package com.postech.auth_service.use_cases;

import com.postech.auth_service.dto.UpdateUserDto;
import com.postech.auth_service.dto.UserDto;
import com.postech.auth_service.repository.UserRepository;
import com.postech.auth_service.use_cases.base.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UpdateUserUseCase implements UseCase<UpdateUserDto, UserDto> {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto execute(UpdateUserDto params) {
        var user = this.repository.getReferenceById(params.getId());
        user.setUsername(params.getUsername());
        user.setPassword(this.passwordEncoder.encode(params.getPassword()));
        user.setName(params.getName());
        user.setEmail(params.getEmail());
        user.setRole(params.getRole());

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
