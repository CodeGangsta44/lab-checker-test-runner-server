package org.kpi.usb.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class PullRequestUser {
    private String login;
    private Long id;
    private Map<String,Integer> numberOfPoolRequestsForEachLab;
}
