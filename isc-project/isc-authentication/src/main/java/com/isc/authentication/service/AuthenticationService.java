package com.isc.authentication.service;

import com.isc.authentication.dto.request.LoginRequest;
import com.isc.authentication.dto.response.CheckEmailResponse;
import com.isc.authentication.dto.response.security.LoggedUser;
import com.isc.authentication.model.Access;
import com.isc.authentication.model.User;
import com.isc.authentication.repository.AccessRepository;
import com.isc.authentication.repository.UserRepository;
import com.isc.authentication.security.JwtTokenUtil;
import com.isc.authentication.security.shared.SecurityPrincipal;
import com.isc.authentication.utils.exception.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtUtils;

    @Autowired
    private AccessRepository accessRepository;

    public LoggedUser authenticate(LoginRequest loginRequest) {
        try {
            Authentication auth = context.getBean(AuthenticationManager.class).authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            User user = (User) loadUserByUsername(loginRequest.getEmail());

            Access access = new Access();
            access.setUsername(user.getUsername());
            access.setPhoneNumber(user.getPhoneNumber());
            access.setEmail(user.getEmail());
            access.setAccessTime(LocalDateTime.now());
            accessRepository.save(access);
            return new LoggedUser(jwtUtils.generateJwt((UserDetails) auth.getPrincipal(), loginRequest.getRememberMe()), user.getName(), user.getSurname());
        } catch (DisabledException e) {
            throw new DisabledException("Disabled User");
        } catch (CredentialsExpiredException e) {
            throw new CredentialsExpiredException("Credentials expired, we ask you kindly to change password");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Wrong credentials. Try again");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));
    }

    public LoggedUser refreshToken() throws UnauthenticatedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            User user = (User) loadUserByUsername(((SecurityPrincipal) auth.getPrincipal()).getUsername());
            if (auth instanceof RememberMeAuthenticationToken) {
                return new LoggedUser(jwtUtils.generateJwt(user, Boolean.TRUE), user.getName(), user.getSurname());
            } else if (!(auth instanceof AnonymousAuthenticationToken)) {
                return new LoggedUser(jwtUtils.generateJwt(user, Boolean.FALSE), user.getName(), user.getSurname());
            } else {
                throw new UnauthenticatedException("Token expired");
            }
        } catch (Exception e) {
            throw new UnauthenticatedException("Authentication error: " + e.getMessage());
        }
    }

    public CheckEmailResponse checkEmail(String email) {
        return new CheckEmailResponse(userRepository.findByEmailAndEnabled(email, true).isPresent());
    }

}
