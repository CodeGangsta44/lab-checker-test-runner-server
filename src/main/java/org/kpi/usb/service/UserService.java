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

    public Optional<Integer> getUserVariantByGithubID(long githubID) {
        final String userVariantEndpoint = "/api/v1/students/{githubID}/variant";

        Map<String, String> params = new HashMap<>();
        params.put("githubID", String.valueOf(githubID));

        RestTemplate restTemplate = new RestTemplate();
        String variant = restTemplate.getForObject(persistenceServerAddr + userVariantEndpoint, String.class, params);

        if (Objects.isNull(variant)) {
            log.warn("There is no variant for such user with githubID: {}", githubID);
            return Optional.empty();
        } else {
            try {
                return Optional.of(Integer.parseInt(variant));
            } catch (NumberFormatException e) {
                log.error("Can not parse user variant", e);
                return Optional.empty();
            }
        }
    }
}
