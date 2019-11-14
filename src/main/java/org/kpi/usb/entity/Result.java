package org.kpi.usb.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result {
    String login;
    String date;
    Long studentID;
    Long variant;
    Long result;
}
