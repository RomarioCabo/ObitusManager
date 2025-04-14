package com.br.obitus_manager.domain.state.service;

import com.br.obitus_manager.domain.state.StateResponse;

import java.util.List;

public interface StateService {

    List<StateResponse> findAllStatesByActive(final boolean active);
}
