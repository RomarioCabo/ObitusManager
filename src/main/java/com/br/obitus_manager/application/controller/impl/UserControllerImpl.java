package com.br.obitus_manager.application.controller.impl;

import com.br.obitus_manager.application.controller.UserController;
import com.br.obitus_manager.application.util.ControllerUtils;
import com.br.obitus_manager.domain.user.UserRequest;
import com.br.obitus_manager.domain.user.UserResponse;
import com.br.obitus_manager.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.obitus_manager.domain.common.PageResponse;

import java.util.UUID;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserControllerImpl extends ControllerUtils implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> save(UserRequest request) {
        log.info("Saving user: {}", request);

        UserResponse userResponse = userService.upsert(null, request, null);

        log.info("User saved: {}", userResponse);

        return ResponseEntity.created(generateUriBuild(userResponse.getId())).body(userResponse);
    }

    @Override
    public ResponseEntity<UserResponse> update(HttpServletRequest httpServletRequest, UserRequest request, UUID userId) {
        log.info("Updating user: {}", request);

        UserResponse userResponse = userService.upsert(userId, request, userId);

        log.info("User updated: {}", userResponse);

        return ResponseEntity.ok(userResponse);
    }

    @Override
    public ResponseEntity<PageResponse<UserResponse>> findAllUsers(
            final Integer page,
            final Integer size,
            final String sort
    ) {
        PageResponse<UserResponse> users = userService.findAll(page, size, sort);

        log.info("Users page {}: {} item(s)", users.getPage(), users.getContent().size());

        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<UserResponse> findById(HttpServletRequest httpServletRequest, UUID userId) {
        UserResponse userResponse = userService.findById(userId, userId);

        log.info("User found: {}", userResponse);

        return ResponseEntity.ok(userResponse);
    }
}
