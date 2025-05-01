package org.example.chatservice.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Копируем cookie из входящего запроса
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attrs != null) {
                HttpServletRequest incomingRequest = attrs.getRequest();
                String cookie = incomingRequest.getHeader("Cookie");
                if (cookie != null) {
                    request.getHeaders().add("Cookie", cookie);
                }
            }

            return execution.execute(request, body);
        }));

        return restTemplate;
    }
}
