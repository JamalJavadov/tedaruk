package az.att.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.IBANValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


@Slf4j
public class IbanValidator implements ConstraintValidator<ValidIban, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isValidIban(value);
    }

    private static boolean isValidIban(String iban) {
        return IBANValidator.DEFAULT_IBAN_VALIDATOR.isValid(iban);
    }


}
