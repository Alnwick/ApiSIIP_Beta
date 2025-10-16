package com.upiicsa.ApiSIIP_Beta.Utils;

import com.upiicsa.ApiSIIP_Beta.Model.UserSIIP;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthHelper {

    public static Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {

            // If the endpoint is protected, this indicates access not authorize
            throw new IllegalStateException("Not authorized for this operation.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserSIIP user) {
            return user.getId();
        }

        throw new IllegalStateException("The principal is not an instance of authenticated user.");
    }
}