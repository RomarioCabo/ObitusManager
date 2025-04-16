package com.br.obitus_manager.infrastructure.persistence.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CustomRepository {
    <T> Page<T> findWithFilters(
            final Class<T> clazz,
            final Map<String, Object> filters,
            final Map<String, Map<String, Object>> subFilters,
            final Pageable pageable
    );
}
