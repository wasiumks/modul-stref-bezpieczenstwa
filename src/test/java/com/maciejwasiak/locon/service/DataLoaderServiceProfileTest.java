package com.maciejwasiak.locon.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DataLoaderServiceProfileTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void dataLoaderService_ShouldNotBeLoaded_InTestProfile() {
        assertThrows(NoSuchBeanDefinitionException.class, () -> {
            applicationContext.getBean(DataLoaderService.class);
        });
    }
}


