package org.kpi.usb.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PullRequest {
    private Student student;
    private Repository repository;
    private String action;
    private String title;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
