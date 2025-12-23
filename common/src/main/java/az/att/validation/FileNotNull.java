package az.att.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = FileNotNullValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface FileNotNull {
    String message() default "File must not be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}