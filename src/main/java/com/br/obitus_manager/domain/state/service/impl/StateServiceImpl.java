package com.br.obitus_manager.domain.state.service.impl;

import com.br.obitus_manager.domain.DatabaseProvider;
import com.br.obitus_manager.domain.common.PageResponse;
import com.br.obitus_manager.domain.state.StateRequest;
import com.br.obitus_manager.domain.state.StateResponse;
import com.br.obitus_manager.domain.state.service.StateService;
import com.br.obitus_manager.domain.util.PagedUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public PageResponse<StateResponse> findAllStatesByActive(
            final Boolean active,
            final Integer page,
            final Integer size,
            final String sort
    ) {
        final Map<String, Object> filters = new HashMap<>();
        Optional.ofNullable(active).ifPresent(value -> filters.put("active", value));
        final Pageable pageable = pagedUtil.getStatePageable(page, size, sort);

        final Page<StateResponse> result = databaseProvider.findAllStates(
                filters, null, pageable, "acronym");

        return PageResponse.from(result);
    }
}
