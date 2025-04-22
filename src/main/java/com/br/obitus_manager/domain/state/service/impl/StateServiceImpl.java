package com.br.obitus_manager.domain.state.service.impl;

import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.state.StateRequest;
import com.br.obitus_manager.domain.state.StateResponse;
import com.br.obitus_manager.domain.state.service.StateService;
import com.br.obitus_manager.domain.util.PagedUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

    private final PagedUtil pagedUtil;
    private final DatabaseProvider databaseProvider;

    @Override
    public List<StateResponse> saveAllStates(final List<StateRequest> stateRequests) {
        return databaseProvider.saveAllStates(stateRequests);
    }

    @Override
    public List<StateResponse> findAllStatesByActive(final Boolean active) {
        final Map<String, Object> filters = new HashMap<>();
        Optional.ofNullable(active).ifPresent(id -> filters.put("active", active));
        final Pageable pageable = pagedUtil.getPageable(null, null);

        return databaseProvider.findAllStates(filters, null, pageable, "acronym");
    }
}
