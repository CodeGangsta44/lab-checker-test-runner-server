package org.kpi.usb.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Student {
    private long id;
    private String login;
}
