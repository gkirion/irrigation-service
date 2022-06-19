package com.george.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.george.model.LandStatus;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

@Service
public class IrrigationService implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(IrrigationService.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String QUEUE_NAME = "sensors-queue";

    @Value("${rabbitmq-host:localhost}")
    private String rabbitMQHost;

    @Autowired
    private ArduinoService arduinoService;

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
            LandStatus landStatus = OBJECT_MAPPER.readValue(message, LandStatus.class);
            LOGGER.info("{}", landStatus);

        }, consumerTag -> { LOGGER.info("consumer shutdown"); });
    }

}
