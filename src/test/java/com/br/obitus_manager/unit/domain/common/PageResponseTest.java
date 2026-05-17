package com.br.obitus_manager.unit.domain.common;

import com.br.obitus_manager.domain.common.PageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageResponseTest {

    @Test
    void fromMapsSpringPageToApiContract() {
        Page<String> springPage = new PageImpl<>(
                List.of("a", "b"),
                PageRequest.of(1, 2),
                5
        );

        PageResponse<String> response = PageResponse.from(springPage);

        assertEquals(List.of("a", "b"), response.getContent());
        assertEquals(1, response.getPage());
        assertEquals(2, response.getSize());
        assertEquals(5, response.getTotalElements());
        assertEquals(3, response.getTotalPages());
        assertFalse(response.isFirst());
        assertFalse(response.isLast());
    }
}
