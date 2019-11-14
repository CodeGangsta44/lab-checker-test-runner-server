package org.kpi.usb.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Repository {
    private Long id;
    private String name;
    private String language;
}
