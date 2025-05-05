package com.example.mugloar.config.client;

import com.example.mugloar.infrastructure.game.client.GameClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class GameClientConfiguration {

    @Value("${api.game-client.base-url}")
    private String apiBaseUrl;

    // TODO: add better http status 410 (game over) handling

    @Bean
    public GameClient gameClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(apiBaseUrl)
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();

        return factory.createClient(GameClient.class);
    }

}
