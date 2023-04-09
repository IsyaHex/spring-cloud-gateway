package uz.epam.msa.gateway.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static uz.epam.msa.gateway.constants.Constants.*;

@Configuration
@Slf4j
public class HttpConfiguration {

    @LoadBalanced
    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public ReactiveDiscoveryClient customDiscoveryClient() {
        return new ReactiveDiscoveryClient() {
            @Override
            public String description() {
                return DISCOVER_CLIENT_DESCRIPTION;
            }

            @Override
            public Flux<ServiceInstance> getInstances(String serviceId) {
                log.info(LOG_FORMAT_TIME_INSTANCE, new Date(), serviceId);
                return Flux.just(portNumbersMap().get(serviceId))
                        .map(port -> new DefaultServiceInstance(serviceId + DASH + port, serviceId,
                                LOCALHOST_LOWERCASE, port, false));
            }

            private Map<String, Integer> portNumbersMap() {
                Map<String, Integer> map = new HashMap<>();
                map.put(SONG_SERVICE_ID, SONG_SERVICE_PORT);
                map.put(RESOURCE_SERVICE_ID, RESOURCE_SERVICE_PORT);
                map.put(STORAGE_SERVICE_ID, STORAGE_SERVICE_PORT);
                return map;
            }

            @Override
            public Flux<String> getServices() {
                return Flux.just(SONG_SERVICE_ID, RESOURCE_SERVICE_ID);
            }
        };
    }

}
