package com.restapi.app.twittor.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import IAuthenticationFacade.IAuthenticationFacade;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {
	
	@Override
    public Authentication getAuthentication() {      
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
