package az.att.search;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class SearchSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = 3522416053866116034L;

    private final List<SearchCriteria> criteriaList;

    public SearchSpecification(List<SearchCriteria> criteriaList) {
        this.criteriaList = new ArrayList<>(criteriaList);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate[] predicates = criteriaList.stream()
                .map(criteria -> criteria.getOperation().buildPredicate(root, criteria, builder))
                .toArray(Predicate[]::new);
        return builder.and(predicates);
    }
}
