package com.george.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.george.model.IrrigationAction;
import com.george.model.IrrigationStatus;
import com.george.model.LandStatus;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final String EXCHANGE_NAME = "commands-exchange";

    @Value("${rabbitmq-host:localhost}")
    private String rabbitMQHost;

    private IrrigationStrategy irrigationStrategy = new IrrigationStrategy(250.0, 650.0);

    @Override
    public void run(String... args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        LOGGER.info("{}", InetAddress.getLocalHost());
        connectionFactory.setHost(rabbitMQHost);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        AMQP.Exchange.DeclareOk declareOk = channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        LOGGER.info("declared exchange {}", declareOk);


        channel.basicConsume(QUEUE_NAME, true, (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            LOGGER.info("consumerTag: {}", consumerTag);
            LOGGER.info("message: {}", message);
            LandStatus landStatus = OBJECT_MAPPER.readValue(message, LandStatus.class);
            LOGGER.info("{}", landStatus);

            IrrigationStatus irrigationStatus = landStatus.getIrrigationStatus();
            IrrigationAction irrigationAction = irrigationStrategy.evaluateAction(landStatus.getMoisture());

            if (irrigationStatus == IrrigationStatus.OFF && irrigationAction == IrrigationAction.START) {
                LOGGER.info("setting irrigation status to: {}", IrrigationStatus.ON);
                channel.basicPublish(EXCHANGE_NAME, landStatus.getPlace(), null, OBJECT_MAPPER.writeValueAsBytes(IrrigationStatus.ON));

            } else if (irrigationStatus == IrrigationStatus.ON && irrigationAction == IrrigationAction.STOP) {
                LOGGER.info("setting irrigation status to: {}", IrrigationStatus.OFF);
                channel.basicPublish(EXCHANGE_NAME, landStatus.getPlace(), null, OBJECT_MAPPER.writeValueAsBytes(IrrigationStatus.OFF));
            }

        }, consumerTag -> { LOGGER.info("consumer shutdown"); });
    }

}
