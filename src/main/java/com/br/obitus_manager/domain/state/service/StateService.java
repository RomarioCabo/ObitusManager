package com.br.obitus_manager.domain.state.service;

import com.br.obitus_manager.domain.common.PageResponse;
import com.br.obitus_manager.domain.state.StateRequest;
import com.br.obitus_manager.domain.state.StateResponse;

import java.util.List;

public interface StateService {

    List<StateResponse> saveAllStates(final List<StateRequest> stateRequests);

    PageResponse<StateResponse> findAllStatesByActive(
            final Boolean active,
            final Integer page,
            final Integer size,
            final String sort
    );
}
