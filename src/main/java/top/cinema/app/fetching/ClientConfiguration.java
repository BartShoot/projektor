package top.cinema.app.fetching;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import top.cinema.app.fetching.cinemacity.api.CinemaCityApiClient;
import top.cinema.app.fetching.helios.api.HeliosApiClient;
import top.cinema.app.fetching.movie_data.api.FilmwebApiClient;
import top.cinema.app.fetching.movie_data.api.ImdbApiClient;
import top.cinema.app.fetching.multikino.api.MultikinoApiClient;

import java.net.http.HttpClient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ClientConfiguration {

    @Value("${cinema-city.base-url}")
    private String cinemaCityBaseUrl;

    @Value("${helios.base-url}")
    private String heliosBaseUrl;

    @Value("${multikino.base-url}")
    private String multikinoBaseUrl;

    @Value("${filmweb.api.url}")
    private String filmwebApiUrl;

    @Value("${imdb.api.url}")
    private String imdbApiUrl;

    @Bean
    CinemaCityApiClient cinemaCityApiClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(cinemaCityBaseUrl)
                .defaultHeader("content-type", "application/json")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();
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
                .baseUrl(heliosBaseUrl)
                .defaultHeader("content-type", "application/json")
                .messageConverters(converters -> converters.addFirst(messageConverter))
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(HeliosApiClient.class);
    }

    @Bean
    MultikinoApiClient multikinoApiClient() {
        var objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
        var messageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        HttpClient httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        RestClient restClient = RestClient.builder()
                .requestFactory(requestFactory)
                //                .requestInterceptor((req, reqBody, ex) -> {
                //                    ClientHttpResponse response = ex.execute(req, reqBody);
                //                    if (!response.getStatusCode().is2xxSuccessful()) {
                //                        log.info("Request body: \n===========\n{}\n===========",
                //                                req.getURI() + " " + new String(reqBody, StandardCharsets.UTF_8));
                //                        log.info("Response body:\n===========\n{}\n===========", new String(
                //                                response.getBody().readAllBytes(), StandardCharsets.UTF_8));
                //                    }
                //                    return response;
                //                })
                .baseUrl(multikinoBaseUrl)
                .defaultHeader("content-type", "application/json")
                .messageConverters(converters -> converters.addFirst(messageConverter))
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(MultikinoApiClient.class);
    }

    @Bean
    FilmwebApiClient filmwebApiClient() {
        RestClient restClient = RestClient.builder().baseUrl(filmwebApiUrl).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(FilmwebApiClient.class);
    }

    @Bean
    ImdbApiClient imdbApiClient() {
        RestClient restClient = RestClient.builder().baseUrl(imdbApiUrl).build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ImdbApiClient.class);
    }
}
