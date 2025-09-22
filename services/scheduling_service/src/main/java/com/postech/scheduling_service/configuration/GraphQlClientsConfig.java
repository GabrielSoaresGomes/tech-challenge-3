// com.postech.scheduling_service.configuration.GraphQlClientsConfig
package com.postech.scheduling_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GraphQlClientsConfig {

    @Bean
    WebClient historyWebClient(@Value("${app.services.history}") String baseUrl) {
        return WebClient.builder().baseUrl(baseUrl).build();
    }

    @Bean
    HttpGraphQlClient historyGraphQlClient(WebClient historyWebClient) {
        return HttpGraphQlClient.builder(historyWebClient).build();
    }
}
