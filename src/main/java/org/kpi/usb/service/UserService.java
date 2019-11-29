package org.kpi.usb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private String persistenceServerAddr;

    public UserService(@Value("${labchecker.persistence-server.address}") String persistenceServerAddr) {
        this.persistenceServerAddr = persistenceServerAddr;
    }

    public Optional<Integer> getUserVariantByGithubIDAndLabRepo(long githubID, String labRepoName) {
        final String userVariantEndpoint = String.format("/api/v1/students/%s/variant?labRepoName=%s", githubID, labRepoName);
        Map<String, Object> params = new HashMap<>();
        params.put("labRepoName", labRepoName);

        RestTemplate restTemplate = new RestTemplate();
        Integer variant = restTemplate.getForObject(persistenceServerAddr + userVariantEndpoint, Integer.class, params);

        if (Objects.isNull(variant)) {
            log.warn("There is no variant for such user with githubID: {}", githubID);
            return Optional.empty();
        } else {
            return Optional.of(variant);
        }
    }
}
