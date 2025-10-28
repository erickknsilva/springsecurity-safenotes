package tech.buildrun.safenotes.controller.dto;

import tech.buildrun.safenotes.entity.Role;
import tech.buildrun.safenotes.entity.Scope;

import java.util.List;

public record ProfileResponse(Long userId,
                              String username,
                              Integer tokenVersion,
                              List<String> scopes,
                              List<String> roles) {
}
