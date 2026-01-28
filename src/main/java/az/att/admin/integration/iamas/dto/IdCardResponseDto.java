package az.att.admin.integration.iamas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdCardResponseDto {
    @JsonProperty("app_name")
    private String appName;
    private String timestamp;
    private String transaction;
    private Integer status;
    private String description;
    private List<IdCardDataWrapperDto> data;
    private Object exception;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IdCardDataWrapperDto {
        private PersonAzDto personAz;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonAzDto {
        private String name;
        private String surname;
    }
}
