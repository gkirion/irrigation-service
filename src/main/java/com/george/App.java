package com.george;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.george.model.SensorValue;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class App implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String QUEUE_NAME = "sensors-queue";

    @Value("${rabbitmq-host:localhost}")
    private String rabbitMQHost;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        LOGGER.info("{}", InetAddress.getLocalHost());
        connectionFactory.setHost(rabbitMQHost);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicConsume(QUEUE_NAME, true, (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                LOGGER.info("consumerTag: {}", consumerTag);
                LOGGER.info("message: {}", message);
                SensorValue sensorValue = OBJECT_MAPPER.readValue(message, SensorValue.class);
                LOGGER.info("{}", sensorValue);
            }, consumerTag -> { LOGGER.info("consumer shutdown"); });
    }

}
