package org.kpi.usb.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PullRequest {
    private PullRequestUser user;
    private LabRepo repo;
    private String date;
    private String message;
    private Long number;
}
