package com.coolightman.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.coolightman.app")
@Import({DatabaseConfiguration.class})
public class AppConfiguration {
}
