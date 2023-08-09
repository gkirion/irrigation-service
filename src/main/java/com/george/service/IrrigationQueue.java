package com.george.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.george.model.LandStatus;
import com.george.model.Status;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

@Service
public class IrrigationQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(IrrigationQueue.class);

    @Value("${rabbitmq-host:localhost}")
    private String rabbitMQHost;

    private static final String QUEUE_NAME = "sensors-queue";

    private static final String EXCHANGE_NAME = "commands-exchange";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Channel channel;

    private Set<LandStatusListener> landStatusListeners = new HashSet<>();

    @PostConstruct
    private void init() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        LOGGER.info("local address: {}, connecting to: {}", InetAddress.getLocalHost(), rabbitMQHost);
        connectionFactory.setHost(rabbitMQHost);
        Connection connection = connectionFactory.newConnection();
        channel = connection.createChannel();

        AMQP.Queue.DeclareOk declareQueueOk = channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        LOGGER.info("declared queue {}", declareQueueOk);

        AMQP.Exchange.DeclareOk declareOk = channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        LOGGER.info("declared exchange {}", declareOk);


        channel.basicConsume(QUEUE_NAME, true, (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            LOGGER.info("consumerTag: {}, message: {}", consumerTag, message);
            LandStatus landStatus = OBJECT_MAPPER.readValue(message, LandStatus.class);
            LOGGER.info("land status: {}", landStatus);

            for (LandStatusListener landStatusListener : landStatusListeners) {
                landStatusListener.receiveLandStatus(landStatus);
            }

        }, consumerTag -> { LOGGER.info("consumer shutdown"); });
    }

    public void sendCommand(String place, Status irrigationStatus) throws IOException {
        LOGGER.info("place: {}, setting irrigation status to: {}", place, irrigationStatus);
        channel.basicPublish(EXCHANGE_NAME, place, null, OBJECT_MAPPER.writeValueAsBytes(irrigationStatus));
    }

    public void addLandStatusListener(LandStatusListener landStatusListener) {
        landStatusListeners.add(landStatusListener);
    }

}
