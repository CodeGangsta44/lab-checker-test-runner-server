package org.kpi.usb.service;

import lombok.extern.slf4j.Slf4j;
import org.kpi.usb.entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ResultService {
    @Value("${labchecker.persistence-server.address}")
    private String persistenceServerAddr;

    public ResultService(@Value("${labchecker.persistence-server.address}") String persistenceServerAddr) {
        this.persistenceServerAddr = persistenceServerAddr;
    }

    public void sendResultToPersistenceServer(Result result) {
        final String postResultEndpoint = "/api/v1/labresults";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForLocation(persistenceServerAddr + postResultEndpoint, result);
    }
}
