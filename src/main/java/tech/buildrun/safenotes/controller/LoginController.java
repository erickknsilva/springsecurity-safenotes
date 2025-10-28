package tech.buildrun.safenotes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.buildrun.safenotes.controller.dto.LoginRequest;
import tech.buildrun.safenotes.controller.dto.LoginResponse;
import tech.buildrun.safenotes.controller.dto.RefreshRequest;
import tech.buildrun.safenotes.service.LoginService;
import tech.buildrun.safenotes.service.RefreshTokenService;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;
    private final RefreshTokenService refreshTokenService;

    public LoginController(LoginService loginService, RefreshTokenService refreshTokenService) {
        this.loginService = loginService;
        this.refreshTokenService = refreshTokenService;
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

}
