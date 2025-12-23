package az.att.util;

import az.att.config.properties.DateProperties;
import az.att.dto.ExpirationDateInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExpirationDateUtil {

    private final DateProperties dateProperties;

    public ExpirationDateInfoDto expirationInfoCounter(Date date) {
        return date == null ? new ExpirationDateInfoDto() : getExpirationDate(date);
    }

    private ExpirationDateInfoDto getExpirationDate(Date date) {
        LocalDate currentDate = LocalDate.now(ZoneId.of(dateProperties.getZoneId()));
        LocalDate convertedDate = date.toInstant().atZone(ZoneId.of(dateProperties.getZoneId())).toLocalDate();
        return currentDate.isAfter(convertedDate)
                ? new ExpirationDateInfoDto() : getExpirationDate(currentDate, convertedDate);
    }

    private ExpirationDateInfoDto getExpirationDate(LocalDate currentDate, LocalDate convertedDate) {
        long totalDays = ChronoUnit.DAYS.between(currentDate, convertedDate);
        Period period = Period.between(currentDate, convertedDate);
        return new ExpirationDateInfoDto(
                period.getYears(),
                period.getMonths(),
                period.getDays(),
                totalDays
        );
    }
}
