package com.postech.auth_service.use_cases;

import com.postech.auth_service.dto.UserDto;
import com.postech.auth_service.repository.UserRepository;
import com.postech.auth_service.use_cases.base.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ListUserUseCase implements UseCase<Void, List<UserDto>> {
    private final UserRepository repository;

    @Override
    public List<UserDto> execute(Void params) {
        var users = this.repository.findAll();

        return users.stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                ))
                .toList();
    }
}
