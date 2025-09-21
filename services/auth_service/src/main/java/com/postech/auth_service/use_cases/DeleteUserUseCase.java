package com.postech.auth_service.use_cases;

import com.postech.auth_service.repository.UserRepository;
import com.postech.auth_service.use_cases.base.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeleteUserUseCase implements UseCase<Long, Void> {
    private final UserRepository repository;

    @Override
    public Void execute(Long id) {
        var user = this.repository.getReferenceById(id);

        this.repository.delete(user);

        return null;
    }
}
