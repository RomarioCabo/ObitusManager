package com.br.obitus_manager.domain.state.service.impl;

import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.state.StateResponse;
import com.br.obitus_manager.domain.state.service.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

    private final DatabaseProvider databaseProvider;

    @Override
    public List<StateResponse> findAllStatesByActive(final boolean active) {
        return databaseProvider.findAllStatesByActive(active);
    }
}
