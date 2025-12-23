package az.att.localization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;

/*
import static az.ingress.common.localization.LocalizationMessagesToDbLoader.DEFAULT_LOCALE_CODE;
*/

@Slf4j
@RequiredArgsConstructor
@Component("messageSource")
public class DBMessageSource extends AbstractMessageSource {

//    private final LanguageRepository languageRepository;

    @Override
    protected MessageFormat resolveCode(String key, Locale locale) {
       /* log.trace("Locate message, {} in locale {}", key, locale);
        String messageStr;
        Optional<LanguageEntity> message = languageRepository.findByLocaleAndMessageKey(locale.getLanguage(), key);
       *//* if (message.isEmpty()) {
            message = languageRepository.findByLocaleAndMessageKey(DEFAULT_LOCALE_CODE, key);
        }*//*
        if (message.isPresent()) {
            messageStr = message.get().getMessage();
        } else {
            messageStr = key;
        }

        log.trace("Found message is : {}", messageStr);
        try {
            return new MessageFormat(messageStr, locale);
        } catch (Exception exception) {
            log.warn("Exception while formatting message for key {} and content {}", key, messageStr);
        }
        return new MessageFormat(key, locale);
    }*/
        return null;
    }
}
