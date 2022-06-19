package com.george.service;

import com.george.exception.ArduinoServiceException;
import com.george.model.IrrigationStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ArduinoWebClient implements ArduinoService {

    @Value("${arduino-service-host:host.docker.internal}")
    private String arduinoServiceHost;

    @Value("${arduino-service-port:8080}")
    private String arduinoServicePort;

    private HttpClient httpClient = HttpClient.newBuilder().build();

    @Override
    public IrrigationStatus getIrrigationStatus() throws ArduinoServiceException {
        HttpRequest httpRequest = HttpRequest
                .newBuilder(URI.create("http://" + arduinoServiceHost + ":" + arduinoServicePort + "/arduino/status"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new ArduinoServiceException(e);
        } catch (InterruptedException e) {
            throw new ArduinoServiceException(e);
        }
        if (response.statusCode() != 200) {
            throw new ArduinoServiceException(String.format("HTTP response status: %d", response.statusCode()));
        }
        return IrrigationStatus.valueOf(response.body().replace("\"", ""));
    }

    @Override
    public IrrigationStatus setIrrigationStatus(IrrigationStatus irrigationStatus) throws ArduinoServiceException {
        HttpRequest httpRequest = HttpRequest
                .newBuilder(URI.create("http://" + arduinoServiceHost + ":" + arduinoServicePort + "/arduino/status"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.format("\"%s\"", irrigationStatus.name())))
                .build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new ArduinoServiceException(e);
        } catch (InterruptedException e) {
            throw new ArduinoServiceException(e);
        }
        if (response.statusCode() != 200) {
            throw new ArduinoServiceException(String.format("HTTP response status: %d", response.statusCode()));
        }
        return IrrigationStatus.valueOf(response.body().replace("\"", ""));
    }

}
