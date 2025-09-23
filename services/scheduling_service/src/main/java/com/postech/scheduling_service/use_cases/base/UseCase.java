package com.postech.scheduling_service.use_cases.base;

public interface UseCase<I, O> {
    O execute(I params);
}
