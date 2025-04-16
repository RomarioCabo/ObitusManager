package com.br.obitus_manager.domain.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PagedUtil {
    public Pageable getPageable(Integer pageIndex, Integer pageSize) {
        if (pageIndex == null || pageSize == null) {
            return Pageable.unpaged();
        }

        return PageRequest.of(pageIndex, pageSize, Sort.unsorted());
    }
}
