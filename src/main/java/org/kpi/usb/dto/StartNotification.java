package org.kpi.usb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StartNotification {
    private String repoName;
    private long githubId;
}
