package com.br.obitus_manager.unit.domain.util;

import com.br.obitus_manager.domain.util.PagedUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PagedUtilTest {

    private PagedUtil pagedUtil;

    @BeforeEach
    void setUp() {
        pagedUtil = new PagedUtil();
    }

    @Test
    void getObituaryPageableUsesDefaults() {
        Pageable pageable = pagedUtil.getObituaryPageable(null, null, null);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals(Sort.by("nameDeceased").ascending(), pageable.getSort());
    }

    @Test
    void getObituaryPageableCapsMaxSize() {
        Pageable pageable = pagedUtil.getObituaryPageable(0, 500, null);
        assertEquals(100, pageable.getPageSize());
    }

    @Test
    void getObituaryPageableResolvesDescSort() {
        Pageable pageable = pagedUtil.getObituaryPageable(1, 5, "dateDeceased,desc");
        assertEquals(1, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
        assertEquals(Sort.by("dateDeceased").descending(), pageable.getSort());
    }

    @Test
    void getObituaryPageableFallsBackToDefaultFieldForInvalidSort() {
        Pageable pageable = pagedUtil.getObituaryPageable(0, 10, "invalidField,asc");
        assertEquals(Sort.by("nameDeceased").ascending(), pageable.getSort());
    }

    @Test
    void getStatePageableUsesAcronymAsDefaultSort() {
        Pageable pageable = pagedUtil.getStatePageable(null, null, null);
        assertEquals(Sort.by("acronym").ascending(), pageable.getSort());
    }

    @Test
    void getUserPageableUsesNameAsDefaultSort() {
        Pageable pageable = pagedUtil.getUserPageable(null, null, null);
        assertEquals(Sort.by("name").ascending(), pageable.getSort());
    }

    @Test
    void getCityPageableUsesNameAsDefaultSort() {
        Pageable pageable = pagedUtil.getCityPageable(null, null, null);
        assertEquals(Sort.by("name").ascending(), pageable.getSort());
    }
}
