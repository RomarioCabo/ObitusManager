package com.br.obitus_manager.domain.state.service;

import com.br.obitus_manager.domain.state.StateRequest;
import com.br.obitus_manager.domain.state.StateResponse;

import java.util.List;

public interface StateService {

    List<StateResponse> saveAllStates(final List<StateRequest> stateRequests);

    List<StateResponse> findAllStatesByActive(final Boolean active);
}
