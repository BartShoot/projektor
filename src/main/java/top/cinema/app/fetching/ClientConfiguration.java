package top.cinema.app.fetching;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import top.cinema.app.fetching.cinemacity.api.CinemaCityApiClient;
import top.cinema.app.fetching.helios.api.HeliosApiClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ClientConfiguration {
    private static final Logger log = LoggerFactory.getLogger("CinemaCityApiClient");

    private static final String CINEMA_CITY_BASE_URL =
            "https://www.cinema-city.pl/pl/data-api-service/v1/quickbook/10103";

    private static final String HELIOS_BASE_URL = "https://api.helios.pl/api/v1";

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

    @Bean
    HeliosApiClient heliosApiClient() {
        var objectMapper = new ObjectMapper();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var messageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        RestClient restClient = RestClient.builder()
                .baseUrl(HELIOS_BASE_URL)
                .defaultHeader("content-type", "application/json")
                .messageConverters(converters -> converters.addFirst(messageConverter))
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(HeliosApiClient.class);
    }
}
