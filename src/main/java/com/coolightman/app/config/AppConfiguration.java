package com.coolightman.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The type App configuration.
 */
@Configuration
@ComponentScan(basePackages = "com.coolightman.app")
@Import({WebConfiguration.class, DatabaseConfiguration.class,
        MessagesConfiguration.class, WebSecurityConfiguration.class})
public class AppConfiguration {

}
