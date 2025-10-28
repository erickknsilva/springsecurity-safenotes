package tech.buildrun.safenotes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.buildrun.safenotes.controller.dto.LoginRequest;
import tech.buildrun.safenotes.controller.dto.LoginResponse;
import tech.buildrun.safenotes.service.LoginService;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {

        // Implement login logic here
        return loginService.login(loginRequest.username(), loginRequest.password());
    }

}
