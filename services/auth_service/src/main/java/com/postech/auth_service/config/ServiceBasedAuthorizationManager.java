package com.postech.auth_service.config;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Set;
import java.util.function.Supplier;

public class ServiceBasedAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private static final Set<String> TRUSTED_SERVICES = Set.of(
            "127.0.0.1"
    );

    private static boolean isGranted(RequestAuthorizationContext context) {
        String remoteIp = context.getRequest().getRemoteAddr();

        return TRUSTED_SERVICES.contains(remoteIp);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        boolean granted = isGranted(context);

        return new AuthorizationDecision(granted);
    }
}
