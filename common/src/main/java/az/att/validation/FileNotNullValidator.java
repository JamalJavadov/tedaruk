package az.att.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;


public class FileNotNullValidator implements ConstraintValidator<FileNotNull, MultipartFile> {

    @Override
    public void initialize(FileNotNull constraintAnnotation) {
        // Initialization, if needed
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        return file != null;
    }
}
