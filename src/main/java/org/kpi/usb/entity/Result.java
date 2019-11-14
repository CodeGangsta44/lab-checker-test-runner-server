package org.kpi.usb.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result {
    String studentLogin;
    String commitDate;
    String labName;
    String language;
    Long studentID;
    Long variant;
    Long result;
}
