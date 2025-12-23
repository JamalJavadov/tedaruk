package az.att.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCriteria {

    private String key;
    private String childKey;
    private String grandChildKey;
    private Object value;
    private SearchOperation operation;
}
