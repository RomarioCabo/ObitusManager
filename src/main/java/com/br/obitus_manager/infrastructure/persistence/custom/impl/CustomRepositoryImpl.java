package com.br.obitus_manager.infrastructure.persistence.custom.impl;

import com.br.obitus_manager.infrastructure.persistence.custom.CustomRepository;
import com.br.obitus_manager.infrastructure.persistence.obituary_notice.ObituaryNoticeEntity;
import com.br.obitus_manager.infrastructure.persistence.state.StateEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.br.obitus_manager.infrastructure.persistence.constants.StringConstantsPersistence.NAME_DECEASED_ENTITY_KEY;

@Repository
public class CustomRepositoryImpl implements CustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final Set<String> VALUES_FOR_LIKE = Set.of(NAME_DECEASED_ENTITY_KEY);

    private final Set<Class<?>> CLASSES_FOR_ORDER_BY = Set.of(ObituaryNoticeEntity.class, StateEntity.class);

    @Override
    @Transactional(readOnly = true)
    public <T> Page<T> findWithFilters(
            final Class<T> clazz,
            final Map<String, Object> filters,
            final Map<String, Map<String, Object>> subFilters,
            final Pageable pageable,
            final String nameForOrderBy
    ) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        final Root<T> root = criteriaQuery.from(clazz);

        final List<Predicate> predicates = buildPredicates(root, filters, subFilters, criteriaBuilder);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        buildOrderBy(clazz, root, criteriaQuery, criteriaBuilder, nameForOrderBy);

        final TypedQuery<T> typedQuery = createTypedQuery(criteriaQuery, pageable);

        final List<T> resultList = typedQuery.getResultList();

        return pageable.isUnpaged() ? new PageImpl<>(resultList)
                : new PageImpl<>(resultList, pageable, getTotalCount(clazz, filters, subFilters));
    }

    private <T> TypedQuery<T> createTypedQuery(final CriteriaQuery<T> criteriaQuery, final Pageable pageable) {
        final TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        if (!pageable.isUnpaged()) {
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());
        }
        return typedQuery;
    }

    private <T> long getTotalCount(final Class<T> clazz, final Map<String, Object> filters,
                                   final Map<String, Map<String, Object>> subFilters) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        final Root<T> countRoot = countQuery.from(clazz);

        countQuery.select(criteriaBuilder.count(countRoot));

        final List<Predicate> predicates = buildPredicates(countRoot, filters, subFilters, criteriaBuilder);
        countQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private <T> void buildOrderBy(final Class<T> clazz, final Root<T> root, final CriteriaQuery<T> criteriaQuery,
                                  final CriteriaBuilder criteriaBuilder, final String nameForOrderBy) {
        if (CLASSES_FOR_ORDER_BY.contains(clazz)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(nameForOrderBy)));
        }
    }

    private <T> List<Predicate> buildPredicates(final Root<T> root, final Map<String, Object> filters,
                                                final Map<String, Map<String, Object>> subFilters,
                                                final CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();

        Optional.ofNullable(filters).orElse(Collections.emptyMap()).forEach((field, value) ->
                predicates.add(buildPredicate(root, field, value, criteriaBuilder)));

        Optional.ofNullable(subFilters).orElse(Collections.emptyMap()).forEach((field, subFiltersMap) ->
                subFiltersMap.forEach((subField, value) ->
                        predicates.add(buildPredicate(root.get(field), subField, value, criteriaBuilder))));

        return predicates;
    }

    private Predicate buildPredicate(final Path<?> path, final String field, final Object value,
                                     final CriteriaBuilder criteriaBuilder) {
        return VALUES_FOR_LIKE.contains(field) ? criteriaBuilder.like(path.get(field), String.format("%s%s%s", "%", value, "%"))
                : criteriaBuilder.equal(path.get(field), value);
    }
}
