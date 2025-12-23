package az.att.dto;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PKClass implements Serializable {

    private UUID id;

    private String lang;
}
