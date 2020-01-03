package com.coolightman.app.component;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class LocalizedMessageSource {
    private List<Locale> localeList = Arrays.asList(new Locale("ru"), new Locale("en"));

    private MessageSource messageSource;

    LocalizedMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String messageCode, Object[] arguments) {
        Locale locale = LocaleContextHolder.getLocale();
        locale = localeList.contains(locale) ? locale : Locale.getDefault();
        return messageSource.getMessage(messageCode, arguments, locale);
    }
}
