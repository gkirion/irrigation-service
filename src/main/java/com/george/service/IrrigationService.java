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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class IrrigationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IrrigationService.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String QUEUE_NAME = "sensors-queue";

    private static final String EXCHANGE_NAME = "commands-exchange";

    @Value("${rabbitmq-host:localhost}")
    private String rabbitMQHost;

    @Autowired
    private IrrigationStrategy irrigationStrategy;

    @Autowired
    private SensorReadingService sensorReadingService;

    private Channel channel;

    private Map<String, IrrigationStatus> irrigationStatusMap = new HashMap<>();

    @PostConstruct
    private void init() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        LOGGER.info("{}", InetAddress.getLocalHost());
        connectionFactory.setHost(rabbitMQHost);
        Connection connection = connectionFactory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        AMQP.Exchange.DeclareOk declareOk = channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        LOGGER.info("declared exchange {}", declareOk);


        channel.basicConsume(QUEUE_NAME, true, (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            LOGGER.info("consumerTag: {}", consumerTag);
            LOGGER.info("message: {}", message);
            LandStatus landStatus = OBJECT_MAPPER.readValue(message, LandStatus.class);
            LOGGER.info("{}", landStatus);

            sensorReadingService.insert(landStatus);
            synchronized (irrigationStatusMap) {
                irrigationStatusMap.put(landStatus.getPlace(), landStatus.getIrrigationStatus());
            }

            IrrigationStatus irrigationStatus = landStatus.getIrrigationStatus();
            IrrigationAction irrigationAction = irrigationStrategy.evaluateAction(landStatus.getPlace(), landStatus.getMoisture());

            if (irrigationStatus == IrrigationStatus.OFF && irrigationAction == IrrigationAction.START) {
                setIrrigationStatus(landStatus.getPlace(), IrrigationStatus.ON);

            } else if (irrigationStatus == IrrigationStatus.ON && irrigationAction == IrrigationAction.STOP) {
                setIrrigationStatus(landStatus.getPlace(), IrrigationStatus.OFF);
            }


        }, consumerTag -> { LOGGER.info("consumer shutdown"); });
    }

    public void setIrrigationStatus(String place, IrrigationStatus irrigationStatus) throws IOException {
        LOGGER.info("place: {}, setting irrigation status to: {}", place, irrigationStatus);
        channel.basicPublish(EXCHANGE_NAME, place, null, OBJECT_MAPPER.writeValueAsBytes(irrigationStatus));
    }

    public IrrigationStatus getIrrigationStatus(String place) {
        synchronized (irrigationStatusMap) {
            return irrigationStatusMap.get(place);
        }
    }

}
