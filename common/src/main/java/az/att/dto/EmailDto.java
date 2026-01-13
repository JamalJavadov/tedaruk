package az.att.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import az.att.dto.EmailVariablesDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

    private String to;
    private String subject;
    private String from;
    private List<EmailVariablesDto> variables;
    private EmailTemplate template;
    private List<String> fileUrls;
}