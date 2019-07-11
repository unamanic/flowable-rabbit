package com.example.asynchistory;

import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class AsyncHistoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncHistoryApplication.class, args);
    }

    @Bean
    public ProcessEngineConfigurationImpl processEngineConfiguration(DataSource dataSource, RabbitMQMessageBasedJobManager jobManager) {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(dataSource);
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_DROP_CREATE);

        // Async history configuration
        config.setAsyncHistoryEnabled(true);
        config.setAsyncHistoryExecutorActivate(true);
        config.setAsyncHistoryExecutorMessageQueueMode(true);

        // Async history JMS settings
        config.setJobManager(jobManager);

        return config;
    }

    @Bean
    public RabbitMQMessageBasedJobManager jobManager(RabbitTemplate rabbitTemplate) {
        RabbitMQMessageBasedJobManager jobManager = new RabbitMQMessageBasedJobManager();
        jobManager.setRabbitTemplate(rabbitTemplate);
        return jobManager;
    }



    private static final String queueName = "flowable-history-jobs";

    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(queueName);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("flowable-exchange");
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory());
        rabbitTemplate.setExchange("flowable-exchange");
        return rabbitTemplate;
    }

}


