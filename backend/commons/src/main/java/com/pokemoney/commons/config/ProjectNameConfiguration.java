package com.pokemoney.commons.config;

import io.micrometer.common.util.StringUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
public class ProjectNameConfiguration implements EnvironmentAware {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        if(StringUtils.isBlank(System.getProperty("project.name"))){
            System.setProperty("project.name",applicationName);
        }
    }
}

