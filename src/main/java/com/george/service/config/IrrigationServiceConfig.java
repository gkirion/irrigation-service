package com.george.service.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeoutException;

@Configuration
public class IrrigationServiceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(IrrigationServiceConfig.class);

    @Value("${rabbitmq-host:localhost}")
    private String rabbitMQHost;

    @Bean
    public Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        LOGGER.info("local address: {}, connecting to: {}", InetAddress.getLocalHost(), rabbitMQHost);
        connectionFactory.setHost(rabbitMQHost);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }

}
