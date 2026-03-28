package com.tongyy.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsConfig {

    @Value("${servers.mq.host}")
    private String host;

    @Value("${servers.mq.port}")
    private int port;

    @Value("${servers.mq.username:guest}")
    private String username;

    @Value("${servers.mq.password:guest}")
    private String password;

    @Value("${servers.mq.queue}")
    private String queueName;

    @Value("${servers.mq.topic}")
    private String topicName;

    @Value("${servers.mq.timeout}")
    private long timeout;

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public Queue appQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(topicName);
    }

    @Bean
    public Binding binding(Queue appQueue, TopicExchange appExchange) {
        return BindingBuilder.bind(appQueue).to(appExchange).with("#");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory rabbitConnectionFactory) {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setReplyTimeout(timeout);
        return template;
    }
}