package com.postech.auth_service.use_cases;

import com.postech.auth_service.dto.UserDto;
import com.postech.auth_service.repository.UserRepository;
import com.postech.auth_service.use_cases.base.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FindUserUseCase implements UseCase<Long, UserDto> {
    private final UserRepository repository;

    @Override
    public UserDto execute(Long id) {
        var user = this.repository.getReferenceById(id);

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
