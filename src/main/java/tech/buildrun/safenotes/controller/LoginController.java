package tech.buildrun.safenotes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tech.buildrun.safenotes.controller.dto.LoginRequest;
import tech.buildrun.safenotes.controller.dto.LoginResponse;
import tech.buildrun.safenotes.controller.dto.LogoutRequest;
import tech.buildrun.safenotes.controller.dto.RefreshRequest;
import tech.buildrun.safenotes.service.LoginService;
import tech.buildrun.safenotes.service.LogoutService;
import tech.buildrun.safenotes.service.RefreshTokenService;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;
    private final RefreshTokenService refreshTokenService;
    private final LogoutService logoutService;

    public LoginController(LoginService loginService, RefreshTokenService refreshTokenService, LogoutService logoutService) {
        this.loginService = loginService;
        this.refreshTokenService = refreshTokenService;
        this.logoutService = logoutService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {

        // Implement login logic here
        return loginService.login(loginRequest.username(), loginRequest.password());
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse refreshToken(@RequestBody RefreshRequest request) {

        return refreshTokenService.refreshToken(request.refreshToken());

    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(@AuthenticationPrincipal Jwt jwt, @RequestBody LogoutRequest request){
        logoutService.logout(jwt, request);
        return ResponseEntity.noContent().build();
    }

}
