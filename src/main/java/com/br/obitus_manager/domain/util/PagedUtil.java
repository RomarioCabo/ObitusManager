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

    private static final Set<String> STATE_SORT_FIELDS = Set.of("name", "acronym", "active");

    private static final Set<String> CITY_SORT_FIELDS = Set.of("name");

    private static final Set<String> USER_SORT_FIELDS = Set.of("name", "email", "createdAt");

    public Pageable getObituaryPageable(
            final Integer pageIndex,
            final Integer pageSize,
            final String sortParam
    ) {
        return getPageable(pageIndex, pageSize, sortParam, "nameDeceased", OBITUARY_SORT_FIELDS);
    }

    public Pageable getStatePageable(
            final Integer pageIndex,
            final Integer pageSize,
            final String sortParam
    ) {
        return getPageable(pageIndex, pageSize, sortParam, "acronym", STATE_SORT_FIELDS);
    }

    public Pageable getCityPageable(
            final Integer pageIndex,
            final Integer pageSize,
            final String sortParam
    ) {
        return getPageable(pageIndex, pageSize, sortParam, "name", CITY_SORT_FIELDS);
    }

    public Pageable getUserPageable(
            final Integer pageIndex,
            final Integer pageSize,
            final String sortParam
    ) {
        return getPageable(pageIndex, pageSize, sortParam, "name", USER_SORT_FIELDS);
    }

    public Pageable getPageable(
            final Integer pageIndex,
            final Integer pageSize,
            final String sortParam,
            final String defaultSortField,
            final Set<String> allowedSortFields
    ) {
        final int page = pageIndex != null && pageIndex >= 0 ? pageIndex : DEFAULT_PAGE;
        int size = pageSize != null && pageSize > 0 ? pageSize : DEFAULT_SIZE;
        if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }

        final Sort sort = resolveSort(sortParam, defaultSortField, allowedSortFields);
        return PageRequest.of(page, size, sort);
    }

    private Sort resolveSort(
            final String sortParam,
            final String defaultSortField,
            final Set<String> allowedSortFields
    ) {
        if (sortParam == null || sortParam.isBlank()) {
            return Sort.by(defaultSortField).ascending();
        }

        final String[] parts = sortParam.split(",", 2);
        String field = parts[0].trim();
        if (!allowedSortFields.contains(field)) {
            field = defaultSortField;
        }

        final boolean descending = parts.length > 1
                && "desc".equalsIgnoreCase(parts[1].trim());

        return descending ? Sort.by(field).descending() : Sort.by(field).ascending();
    }
}
