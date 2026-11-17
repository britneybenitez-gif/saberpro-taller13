package co.edu.unicauca.saberpro.lifecycle.messaging;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopology {
    @Bean
    Declarables reviewTopology(@Value("${app.messaging.review-queue}") String queue) {
        String dlxName = queue + ".dlx";
        String dlqName = queue + ".dlq";
        Queue reviews = QueueBuilder.durable(queue).deadLetterExchange(dlxName)
                .deadLetterRoutingKey(dlqName).build();
        DirectExchange dlx = new DirectExchange(dlxName, true, false);
        Queue deadLetters = QueueBuilder.durable(dlqName).build();
        return new Declarables(reviews, dlx, deadLetters,
                BindingBuilder.bind(deadLetters).to(dlx).with(dlqName));
    }
}
