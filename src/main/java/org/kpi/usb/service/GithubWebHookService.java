package org.kpi.usb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.kpi.usb.entity.PullRequest;
import org.kpi.usb.entity.Repository;
import org.kpi.usb.entity.Student;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Slf4j
public class GithubWebHookService {
    private final ObjectMapper objectMapper;

    public GithubWebHookService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Student getStudentFromJSON(String content) {
        try {
            JsonNode root = objectMapper.readTree(content);
            return Student.builder()
                    .id(root.at("/sender/id").asLong())
                    .login(root.at("/sender/login").asText())
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Can not parse content");
        }
    }

    public Repository getRepositoryFromJSON(String content) {
        try {
            JsonNode root = objectMapper.readTree(content);
            return Repository.builder()
                    .id(root.at("/repository/id").asLong())
                    .name(root.at("/repository/name").asText())
                    .language(root.at("/repository/language").asText())
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Can not parse content");
        }
    }

    public PullRequest getPullRequestFromJSON(String content) {
        try {
            JsonNode root = objectMapper.readTree(content);
            Instant createdInstant = Instant.parse(root.at("/pull_request/created_at").asText());
            Instant updatedInstant = Instant.parse(root.at("/pull_request/updated_at").asText());
            return PullRequest.builder()
                    .student(getStudentFromJSON(content))
                    .repository(getRepositoryFromJSON(content))
                    .action(root.at("/action").asText())
                    .title(root.at("/pull_request/title").asText())
                    .createdDate(LocalDateTime.ofInstant(createdInstant, ZoneId.systemDefault()))
                    .updatedDate(LocalDateTime.ofInstant(updatedInstant, ZoneId.systemDefault()))
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Can not parse content");
        }
    }
}