package az.att.dto;

import az.att.search.SearchCriteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNullElseGet;

public class GenericSearchDto {

    private List<SearchCriteria> criteria = new ArrayList<>();

    public List<SearchCriteria> getCriteria() {
        return Collections.unmodifiableList(criteria);
    }

    public final void setCriteria(List<SearchCriteria> criteria) {
        this.criteria = new ArrayList<>(requireNonNullElseGet(criteria, Collections::emptyList));
    }

    public void addCriteria(SearchCriteria element) {
        criteria.add(element);
    }
}
