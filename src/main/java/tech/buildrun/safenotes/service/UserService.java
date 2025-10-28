package tech.buildrun.safenotes.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import tech.buildrun.safenotes.controller.dto.ProfileResponse;
import tech.buildrun.safenotes.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProfileResponse readProfile(Jwt jwt) {

        var user = userRepository.findById(Long.valueOf(jwt.getSubject()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ProfileResponse(user.getId(),
                user.getUsername(),
                user.getTokenVersion(),
                jwt.getClaimAsStringList("scope"),
                jwt.getClaimAsStringList("roles"));
    }
}
