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
import java.util.HashSet;
import java.util.Set;

@Service
public class IrrigationService implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(IrrigationService.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String QUEUE_NAME = "sensors-queue";

    @Value("${rabbitmq-host:localhost}")
    private String rabbitMQHost;

    private IrrigationStrategy irrigationStrategy = new IrrigationStrategy(250.0, 650.0);

    private Set<String> registeredExchanges = new HashSet<>();

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

            if (!registeredExchanges.contains(landStatus.getPlace())) {
                AMQP.Exchange.DeclareOk declareOk = channel.exchangeDeclare(landStatus.getPlace(), "fanout");
                LOGGER.info("declared exchange {}", declareOk);
                registeredExchanges.add(landStatus.getPlace());
            }

            IrrigationStatus irrigationStatus = landStatus.getIrrigationStatus();
            IrrigationAction irrigationAction = irrigationStrategy.evaluateAction(landStatus.getMoisture());

            if (irrigationStatus == IrrigationStatus.OFF && irrigationAction == IrrigationAction.START) {
                channel.basicPublish(landStatus.getPlace(), "", null, OBJECT_MAPPER.writeValueAsBytes(IrrigationStatus.ON));

            } else if (irrigationStatus == IrrigationStatus.ON && irrigationAction == IrrigationAction.STOP) {
                channel.basicPublish(landStatus.getPlace(), "", null, OBJECT_MAPPER.writeValueAsBytes(IrrigationStatus.OFF));
            }

        }, consumerTag -> { LOGGER.info("consumer shutdown"); });
    }

}
