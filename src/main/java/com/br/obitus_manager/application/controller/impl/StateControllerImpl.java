package com.br.obitus_manager.application.controller.impl;

import com.br.obitus_manager.application.controller.StateController;
import com.br.obitus_manager.domain.state.StateResponse;
import com.br.obitus_manager.domain.state.service.StateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StateControllerImpl implements StateController {

    private final StateService stateService;

    @Override
    public ResponseEntity<List<StateResponse>> findAllStatesByActive(Boolean active) {
        List<StateResponse> states = stateService.findAllStatesByActive(active);

        log.info("States: {}", states);

        return ResponseEntity.ok(states);
    }
}
