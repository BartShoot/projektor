package top.cinema.app.fetching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import top.cinema.app.fetching.cinemacity.api.CinemaCityApiClient;

@Configuration
public class ClientConfiguration {
    private static final Logger log = LoggerFactory.getLogger("CinemaCityApiClient");

    private static final String CINEMA_CITY_BASE_URL =
            "https://www.cinema-city.pl/pl/data-api-service/v1/quickbook/10103";

    @Bean
    CinemaCityApiClient cinemaCityApiClient() {
        RestClient restClient = RestClient.builder()
                                          .baseUrl(CINEMA_CITY_BASE_URL)
                                          .defaultHeader("content-type", "application/json")
                                          .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(CinemaCityApiClient.class);
    }
}
