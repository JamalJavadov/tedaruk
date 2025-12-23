package az.att.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Collection;

public enum SearchOperation {

    GREATER_THAN {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
        }
    },
    LESS_THAN {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
        }
    },
    GREATER_THAN_EQUAL {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        }
    },
    LESS_THAN_EQUAL {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        }
    },
    NOT_EQUAL {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.notEqual(root.get(criteria.getKey()), criteria.getValue().toString());
        }
    },
    EQUAL {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.equal(root.get(criteria.getKey()),
                    castToRequiredType(root.get(criteria.getKey()).getJavaType(), criteria.getValue()));
        }
    },
    JOIN_GRAND_CHILD {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            Join<Object, Object> join =
                    root.join(criteria.getKey()).join(criteria.getChildKey());
            if (join.get(criteria.getGrandChildKey()).getJavaType().isAssignableFrom(Long.class)
                    || join.get(criteria.getGrandChildKey()).getJavaType().isAssignableFrom(Double.class)) {
                return builder.equal(join.get(criteria.getGrandChildKey()), criteria.getValue());
            }
            if (Enum.class.isAssignableFrom(join.get(criteria.getGrandChildKey()).getJavaType())) {
                return builder.equal(join.get(criteria.getGrandChildKey()),
                        castToRequiredType(join.get(criteria.getGrandChildKey()).getJavaType(), criteria.getValue()));
            }
            return builder.like(builder.lower(join.get(criteria.getGrandChildKey())),
                    PERCENT_SIGN + criteria.getValue().toString().toLowerCase() + PERCENT_SIGN);
        }
    },
    JOIN_CHILD {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            Join<Object, Object> join =
                    root.join(criteria.getKey());
            if (join.get(criteria.getChildKey()).getJavaType().isAssignableFrom(Long.class)
                    || join.get(criteria.getChildKey()).getJavaType().isAssignableFrom(Double.class)) {
                return builder.equal(join.get(criteria.getChildKey()), criteria.getValue());
            }

            if (Enum.class.isAssignableFrom(join.get(criteria.getChildKey()).getJavaType())) {
                return builder.equal(join.get(criteria.getChildKey()),
                        castToRequiredType(join.get(criteria.getChildKey()).getJavaType(), criteria.getValue()));
            }
            return builder.like(builder.lower(join.get(criteria.getChildKey())),
                    PERCENT_SIGN + criteria.getValue().toString().toLowerCase() + PERCENT_SIGN);
        }
    },
    MATCH {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.like(builder.lower(root.get(criteria.getKey())),
                    PERCENT_SIGN + criteria.getValue().toString().toLowerCase() + PERCENT_SIGN);
        }
    },
    MATCH_START {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.like(builder.lower(root.get(criteria.getKey())),
                    criteria.getValue().toString().toLowerCase() + PERCENT_SIGN);
        }
    },
    MATCH_END {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.like(builder.lower(root.get(criteria.getKey())),
                    PERCENT_SIGN + criteria.getValue().toString().toLowerCase());
        }
    },
    IN {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.in(root.get(criteria.getKey())).value(criteria.getValue());
        }
    },
    NOT_IN {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            return builder.not(root.get(criteria.getKey())).in(criteria.getValue());
        }
    },
    ANY_OF {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            Object value = criteria.getValue();
            if (!(value instanceof Collection)) {
                throw new IllegalArgumentException("ANY_OF operation is allowed on the collection only");
            }
            Collection<?> elements = (Collection<?>) value;
            Predicate[] predicates = elements.stream()
                    .map(v -> builder.equal(root.get(criteria.getKey()), v))
                    .toArray(Predicate[]::new);
            return builder.or(predicates);
        }
    },
    IS_MEMBER {
        @Override
        public Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder) {
            Object value = criteria.getValue();
            if (!(value instanceof Collection)) {
                throw new IllegalArgumentException("IS_MEMBER operation is allowed on the collection only");
            }
            Collection<?> elements = (Collection<?>) value;
            Predicate[] predicates = elements.stream()
                    .map(v -> builder.isMember(v, root.get(criteria.getKey())))
                    .toArray(Predicate[]::new);
            return builder.or(predicates);
        }
    };

    private static final String PERCENT_SIGN = "%";

    public abstract Predicate buildPredicate(Root<?> root, SearchCriteria criteria, CriteriaBuilder builder);

    public Object castToRequiredType(Class fieldType, Object value) {
        if (Enum.class.isAssignableFrom(fieldType)) {
            return Enum.valueOf(fieldType, value.toString());
        }
        return value;
    }
}

