package com.example.asynclistener;

import org.flowable.common.engine.impl.interceptor.CommandExecutor;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.job.service.impl.history.async.message.AsyncHistoryJobMessageReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AsyncListenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncListenerApplication.class, args);
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
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        container.setConcurrentConsumers(8);
        container.start();
        return container;
    }

    @Bean
    public Receiver receiver(AsyncHistoryJobMessageReceiver asyncHistoryJobMessageReceiver, ProcessEngineConfiguration processEngineConfiguration) {
        Receiver receiver = new Receiver();
        receiver.setAsyncHistoryJobMessageReceiver(asyncHistoryJobMessageReceiver);
        receiver.setProcessEngineConfiguration(processEngineConfiguration);
        return receiver;
    }

    @Bean
    public AsyncHistoryJobMessageReceiver asyncHistoryJobMessageReceiver() {
        AsyncHistoryJobMessageReceiver asyncHistoryJobMessageReceiver = new AsyncHistoryJobMessageReceiver();
        asyncHistoryJobMessageReceiver.setAsyncHistoryJobMessageHandler(myJobMessageHandler());
        return asyncHistoryJobMessageReceiver;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    public MyJobMessageHandler myJobMessageHandler() {
        return new MyJobMessageHandler();
    }

}
