package com.postech.history.graphql;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {
    @Bean
    RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiring -> wiring.scalar(ExtendedScalars.DateTime);
    }
}
