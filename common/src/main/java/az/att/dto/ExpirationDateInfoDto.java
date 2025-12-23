package az.att.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpirationDateInfoDto implements Serializable {

    private static final long serialVersionUID = -2530833150273180231L;

    private int year;
    private int month;
    private int days;
    private long totalDays;
}
