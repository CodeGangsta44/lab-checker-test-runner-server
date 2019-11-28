package org.kpi.usb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class LabService {
    private String persistenceServerAddr;

    public LabService(@Value("${labchecker.persistence-server.address}") String persistenceServerAddr) {
        this.persistenceServerAddr = persistenceServerAddr;
    }

    public Optional<Integer> getMaxMarkForLab(String labRepoName) {
        final String getMarkEndpoint = String.format("/api/v1/labs/%s/max-mark", labRepoName);
        RestTemplate restTemplate = new RestTemplate();
        Integer maxMark = restTemplate.getForObject(persistenceServerAddr + getMarkEndpoint, Integer.class);
        return Optional.ofNullable(maxMark);
    }

    public Optional<String> getTestRepoNameForLab(String labRepoName) {
        final String getTestRepoNameEndpoint = String.format("/api/v1/labs/%s/test-repo", labRepoName);
        RestTemplate restTemplate = new RestTemplate();
        String testRepoName = restTemplate.getForObject(persistenceServerAddr + getTestRepoNameEndpoint, String.class);
        return Optional.ofNullable(testRepoName);
    }
}
