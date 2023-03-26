package com.markergus.mainserver.util.client;

import com.markergus.mainserver.configurations.TokenServiceConfigProperties;
import com.markergus.mainserver.dto.GenTokenRequestDto;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;


@Component
public class GenTokenClient {

    private final TokenServiceConfigProperties tokenServiceConfigProperties;
    private final WebClient webClient;

    @Autowired
    public GenTokenClient(TokenServiceConfigProperties tokenServiceConfigProperties) {
        this.tokenServiceConfigProperties = tokenServiceConfigProperties;
        // Pretty small api call so pretty quick timeout
        HttpClient client = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofSeconds(tokenServiceConfigProperties.getTimeout()));
        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl(tokenServiceConfigProperties.getBaseUrl())
                .build();
    }

    public Mono<String> submitGenTokenRequest(GenTokenRequestDto genTokenRequestDto) {
        return webClient.post()
                .uri(tokenServiceConfigProperties.getApi(), genTokenRequestDto)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(genTokenRequestDto), GenTokenRequestDto.class)
                .retrieve()
                .bodyToMono(String.class);
    }
}
