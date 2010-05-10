package com.goodworkalan.infuse.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

public class InfuseProject extends ProjectModule {
    @Override
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.reflective/permeate/0.1")
                .test()
                    .depends()
                        .include("org.testng/testng-jdk15/5.10")
                        .include("org.mockito/mockito-core/1.6")
                        .end()
                    .end()
                .end()
            .end();
    }
}
