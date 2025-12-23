package az.att.validation;

import org.apache.commons.lang3.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneConstraintValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public void initialize(ValidPhone arg0) {
        //initializer
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        String regex = "^(\\+99460|\\+99410|\\+99450|\\+99451|\\+99455|\\+99470|\\+99477|\\+99499)([2-9])(\\d{4})(\\d{2})$";
        return StringUtils.isNoneBlank(phone) && Pattern.compile(regex).matcher(phone).matches();
    }
}
