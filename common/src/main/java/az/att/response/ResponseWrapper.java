package az.att.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper<T> {

    private Boolean dataExists;
    private T data;

    private T getContent() {
        return data;
    }

    private void setContent(T data) {
        this.data = data;
    }
}
