package com.coolightman.app.component;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * The type Localized message source.
 */
@Component
public class LocalizedMessageSource {
    private List<Locale> localeList = Arrays.asList(new Locale("ru"), new Locale("en"));

    private MessageSource messageSource;

    /**
     * Instantiates a new Localized message source.
     *
     * @param messageSource the message source
     */
    LocalizedMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Gets message.
     *
     * @param messageCode the message code
     * @param arguments   the arguments
     * @return the message
     */
    public String getMessage(String messageCode, Object[] arguments) {
        Locale locale = LocaleContextHolder.getLocale();
        locale = localeList.contains(locale) ? locale : Locale.getDefault();
        return messageSource.getMessage(messageCode, arguments, locale);
    }
}
