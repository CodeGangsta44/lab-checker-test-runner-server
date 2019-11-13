package org.kpi.usb.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LabRepo {
    private String name;
    private Long id;
    private String language;
    private Long forksCount;
}
