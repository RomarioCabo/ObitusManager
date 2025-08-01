package com.br.obitus_manager.infrastructure.persistence.custom;

import com.br.obitus_manager.infrastructure.persistence.custom.impl.CustomRepositoryImpl;
import com.br.obitus_manager.infrastructure.persistence.obituary_notice.ObituaryNoticeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomRepositoryTest {

    @InjectMocks
    private CustomRepositoryImpl customRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<ObituaryNoticeEntity> criteriaQuery;

    @Mock
    private Root<ObituaryNoticeEntity> root;

    @Mock
    private Predicate predicate;

    @Mock
    private TypedQuery<ObituaryNoticeEntity> typedQuery;

    @Mock
    private CriteriaQuery<Long> countQuery;

    @Mock
    private Root<ObituaryNoticeEntity> countRoot;

    @Test
    void testFindWithFiltersPagedSuccess() {
        Map<String, Object> filters = Map.of("field", "value");
        Map<String, Map<String, Object>> subFilters = Map.of();
        Pageable pageable = PageRequest.of(0, 10);
        String orderBy = "name";

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(ObituaryNoticeEntity.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(ObituaryNoticeEntity.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(new ObituaryNoticeEntity()));
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Long.class)).thenReturn(countQuery);
        when(countQuery.from(ObituaryNoticeEntity.class)).thenReturn(countRoot);
        when(criteriaBuilder.count(countRoot)).thenReturn(mock(Expression.class));
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);
        when(entityManager.createQuery(countQuery)).thenReturn(mock(TypedQuery.class));
        when(entityManager.createQuery(countQuery).getSingleResult()).thenReturn(1L);

        Page<ObituaryNoticeEntity> result = customRepository.findWithFilters(
                ObituaryNoticeEntity.class,
                filters,
                subFilters,
                pageable,
                orderBy
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}

