package org.kpi.usb.service;

import org.kpi.usb.dto.StartNotification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    private String persistenceServerAddr;

    public NotificationService(@Value("${labchecker.persistence-server.address}") String persistenceServerAddr) {
        this.persistenceServerAddr = persistenceServerAddr;
    }

    public void notifyAboutStartOfBuild(String repoName, long githubId) {
        final String postResultEndpoint = "/api/v1/notifications/start";

        RestTemplate restTemplate = new RestTemplate();
        StartNotification startNotification = StartNotification.builder()
                .repoName(repoName)
                .githubId(githubId)
                .build();

        restTemplate.postForLocation(persistenceServerAddr + postResultEndpoint, startNotification);
    }
}
