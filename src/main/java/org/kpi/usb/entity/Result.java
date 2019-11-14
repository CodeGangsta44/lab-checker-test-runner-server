package org.kpi.usb.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Result {
    String studentGithubLogin;
    long studentGithubID;
    int variant;
    int mark;
    String repositoryName;
    String language;
    LocalDateTime updatedDate;
}
