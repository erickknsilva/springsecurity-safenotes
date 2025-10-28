package tech.buildrun.safenotes.controller.dto;

import java.util.List;

public record LoginResponse(
        String accessToken,
        Long expiresIn,
        String refreshToken,
        Long refreshExpiresIn,
        List<String>  scopes
) {
}
