package az.att.admin.integration.asan.login.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserJwtDto {
    private String pin;

    @JsonProperty("name")
    private String firstName;

    @JsonProperty("surname")
    private String lastName;

    private String patronymic;
    private String citizenship;
    private String phone;
}
