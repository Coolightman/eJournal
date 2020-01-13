package com.coolightman.app.config;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

/**
 * The type Web configuration.
 */
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    private ApplicationContext applicationContext;

    /**
     * Instantiates a new Web configuration.
     *
     * @param applicationContext the application context
     */
    public WebConfiguration(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Mapper mapper.
     *
     * @return the mapper
     */
    @Bean
    public Mapper mapper() {
        return new DozerBeanMapper();
    }

    /**
     * Template resolver spring resource template resolver.
     *
     * @return the spring resource template resolver
     */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    /**
     * Template engine spring template engine.
     *
     * @return the spring template engine
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine());
        thymeleafViewResolver.setCharacterEncoding("UTF-8");
        thymeleafViewResolver.setContentType("text/html;charset=UTF-8");
        registry.viewResolver(thymeleafViewResolver);
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**", "/css/**")
                .addResourceLocations("/WEB-INF/views/img/", "/WEB-INF/views/css/");
    }
}
