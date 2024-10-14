package pe.edu.cibertec.patitas_frontend_wc_a.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // timeout de conexión
            .responseTimeout(Duration.ofSeconds(10)) // timeout para obtener el total de la respuesta
            .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))); // timeout para la recepción de cada paquete

    @Bean
    public WebClient webClientAutenticacion(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8095/autenticacion")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

}
