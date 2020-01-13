package com.coolightman.app.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * The type Messages configuration.
 */
public class MessagesConfiguration {

    /**
     * Message source message source.
     *
     * @return the message source
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("applicationMessages");
        return messageSource;
    }
}
