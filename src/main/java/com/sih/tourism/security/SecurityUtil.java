package com.sih.tourism.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    /**
     * Retrieves the authenticated user's ID from the JWT-backed SecurityContext.
     * Controllers must use this instead of accepting a userId from the client
     * for any endpoint dealing with the caller's own personal data.
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUserId();
    }
}
