package com.app.dummyPack.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

@Component
public class envpostprocess implements EnvironmentPostProcessor {

    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication spa) {

        env.setActiveProfiles("dev");
    }
}
