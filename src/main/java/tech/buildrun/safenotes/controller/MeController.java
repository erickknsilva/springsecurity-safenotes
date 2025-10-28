package tech.buildrun.safenotes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tech.buildrun.safenotes.controller.dto.ProfileResponse;
import tech.buildrun.safenotes.service.UserService;

@RestController
@RequestMapping("/me")
public class MeController {

    private final UserService userservice;

    public MeController(UserService userservice) {
        this.userservice = userservice;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('SCOPE_PROFILE:READ')")
    public ProfileResponse getProfile(@AuthenticationPrincipal Jwt jwt) {

        return  userservice.readProfile(jwt);
    }
}
