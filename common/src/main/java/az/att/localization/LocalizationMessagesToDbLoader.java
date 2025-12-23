/*
package az.ingress.common.localization;

import az.ingress.common.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocalizationMessagesToDbLoader {

    public static final String DEFAULT_LOCALE_CODE = "en";
*/
/*
    private final LanguageRepository languageRepository;
*//*


    public void loadMessagesIntoDb() {
        Reflections reflections = new Reflections("az.ingress.");
        Set<Class<? extends ErrorResponse>> classes = reflections.getSubTypesOf(ErrorResponse.class);

        classes.stream()
                .peek(clazz -> log.trace("Loading localization messages from class {}", clazz))
                .map(Class::getEnumConstants)
                .flatMap(Arrays::stream)
                .forEach(this::saveEntry);
    }

    private void saveEntry(ErrorResponse errorResponse) {
        LanguageEntity entity = languageRepository.findByLocaleAndMessageKey(DEFAULT_LOCALE_CODE,
                        errorResponse.getKey())
                .orElse(new LanguageEntity());
        LanguageEntity languageEntity = LanguageEntity.builder()
                .id(entity.getId())
                .messageKey(errorResponse.getKey())
                .locale(DEFAULT_LOCALE_CODE)
                .message(errorResponse.getMessage())
                .build();
        languageRepository.save(languageEntity);
    }
}
*/
