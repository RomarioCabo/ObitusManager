package com.br.obitus_manager.domain.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PagedUtil {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 100;

    private static final Set<String> OBITUARY_SORT_FIELDS = Set.of(
            "nameDeceased", "dateDeceased", "dateTimeBurial"
    );

    /**
     * Sem {@code page}/{@code size} explícitos retorna não paginado (estados/cidades).
     * Endpoints paginados devem usar {@link #getObituaryPageable} com parâmetros do controller.
     */
    public Pageable getPageable(final Integer pageIndex, final Integer pageSize) {
        if (pageIndex == null && pageSize == null) {
            return Pageable.unpaged();
        }
        return getPageable(pageIndex, pageSize, null, "nameDeceased");
    }

    public Pageable getPageable(
            final Integer pageIndex,
            final Integer pageSize,
            final String sortParam,
            final String defaultSortField
    ) {
        final int page = pageIndex != null && pageIndex >= 0 ? pageIndex : DEFAULT_PAGE;
        int size = pageSize != null && pageSize > 0 ? pageSize : DEFAULT_SIZE;
        if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }

        final Sort sort = resolveSort(sortParam, defaultSortField);
        return PageRequest.of(page, size, sort);
    }

    public Pageable getObituaryPageable(
            final Integer pageIndex,
            final Integer pageSize,
            final String sortParam
    ) {
        return getPageable(pageIndex, pageSize, sortParam, "nameDeceased");
    }

    private Sort resolveSort(final String sortParam, final String defaultSortField) {
        if (sortParam == null || sortParam.isBlank()) {
            return Sort.by(defaultSortField).ascending();
        }

        final String[] parts = sortParam.split(",", 2);
        String field = parts[0].trim();
        if (!OBITUARY_SORT_FIELDS.contains(field)) {
            field = defaultSortField;
        }

        final boolean descending = parts.length > 1
                && "desc".equalsIgnoreCase(parts[1].trim());

        return descending ? Sort.by(field).descending() : Sort.by(field).ascending();
    }
}
