package com.tongyy;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@SpringBootApplication
@EnableScheduling
public class Application {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriverClassName;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(dbUrl);
        ds.setUsername(dbUsername);
        ds.setPassword(dbPassword);
        ds.setDriverClassName(dbDriverClassName);
        return ds;
    }

    // Listener container for the queue defined in JmsConfig
    @Bean(name = "queueContainer")
    public SimpleMessageListenerContainer queueContainer(ConnectionFactory connectionFactory,
                                                         Queue appQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(appQueue.getName());
        container.setAutoStartup(true);
        return container;
    }

    // Listener container for the topic exchange defined in JmsConfig
    @Bean(name = "topicContainer")
    public SimpleMessageListenerContainer topicContainer(ConnectionFactory connectionFactory,
                                                         Queue appQueue,
                                                         TopicExchange appExchange) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        // Reuse the same queue bound to the topic exchange via JmsConfig binding
        container.setQueueNames(appQueue.getName());
        container.setAutoStartup(true);
        return container;
    }
}