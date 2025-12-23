package az.att.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileResponseDto {

    String username;
    String firstName;
    String avatar;
    String phone;
    String lastName;
}
