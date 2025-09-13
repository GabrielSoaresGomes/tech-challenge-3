package com.postech.auth_service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.net.InetAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
public class ServiceBasedAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private static final Map<String, String> verifiedHosts = new HashMap<>();
    private static List<String> trustedHosts = List.of();

    public ServiceBasedAuthorizationManager(List<String> trustedHosts) {
        ServiceBasedAuthorizationManager.trustedHosts = trustedHosts;
    }

    private static boolean isGranted(RequestAuthorizationContext context) {
        var remoteIp = context.getRequest().getRemoteAddr();

        if (verifiedHosts.containsValue(remoteIp)) {
            return true;
        } else if (Objects.equals(verifiedHosts.size(), trustedHosts.size())) {
            return false;
        }

        for (var trustedHost : trustedHosts) {
            if (verifiedHosts.containsKey(trustedHost)) {
                continue;
            }

            try {
                var host = URI.create(trustedHost).getHost();
                var inet = InetAddress.getByName(host);
                var hostAddress = inet.getHostAddress();
                verifiedHosts.put(trustedHost, hostAddress);

                if (hostAddress.equals(remoteIp)) {
                    return true;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        return false;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        boolean granted = isGranted(context);

        return new AuthorizationDecision(granted);
    }
}
