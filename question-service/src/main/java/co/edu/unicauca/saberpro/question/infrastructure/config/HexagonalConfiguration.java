package co.edu.unicauca.saberpro.question.infrastructure.config;

import co.edu.unicauca.saberpro.question.application.QuestionApplicationService;
import co.edu.unicauca.saberpro.question.domain.ports.outbound.*;
import co.edu.unicauca.saberpro.question.infrastructure.jpa.*;
import co.edu.unicauca.saberpro.question.infrastructure.memory.InMemoryQuestionAdapter;
import co.edu.unicauca.saberpro.question.infrastructure.messaging.RabbitMQPublisher;
import co.edu.unicauca.saberpro.question.infrastructure.validation.RestValidationAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class HexagonalConfiguration {
    @Bean
    @ConditionalOnProperty(name = "app.repository", havingValue = "jpa", matchIfMissing = true)
    JpaQuestionAdapter jpaQuestionPort(SpringDataPreguntaRepository jpa) {
        return new JpaQuestionAdapter(jpa);
    }

    @Bean
    @ConditionalOnProperty(name = "app.repository", havingValue = "memory")
    InMemoryQuestionAdapter memoryQuestionPort() { return new InMemoryQuestionAdapter(); }

    @Bean
    ValidacionContenidoPort validacionContenidoPort(RestClient.Builder builder,
            @Value("${app.validation.base-url}") String baseUrl,
            @Value("${app.validation.timeout:3s}") Duration timeout) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        return new RestValidationAdapter(builder.baseUrl(baseUrl).requestFactory(factory).build());
    }

    @Bean
    NotificacionRevisionPort notificacionRevisionPort(RabbitTemplate template, ObjectMapper mapper,
            @Value("${app.messaging.review-queue}") String queue,
            @Value("${app.messaging.confirm-timeout:5s}") Duration timeout) {
        template.setMandatory(true);
        return new RabbitMQPublisher(template, mapper, queue, timeout);
    }

    @Bean
    QuestionApplicationService questionApplicationService(PreguntaRepositoryPort repo,
            NotificacionRevisionPort notificaciones, ValidacionContenidoPort validacion) {
        return new QuestionApplicationService(repo, notificaciones, validacion);
    }
}
