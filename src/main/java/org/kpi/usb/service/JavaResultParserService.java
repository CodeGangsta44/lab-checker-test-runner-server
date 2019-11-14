package org.kpi.usb.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JavaResultParserService {
    private static final String FIELD_TOTAL_RUN = "Tests run";
    private static final String FIELD_FAILED = "Failures";
    private static final String FIELD_ERROR = "Errors";
    private static final String FIELD_SKIPPED = "Skipped";
    private static final String INTERNAL_FIELD_DELIMITER = ":=";

    public Integer getResult(List<String> resultFile, Integer maxMark) {
        return getMark(getResultMap(getSummaryString(resultFile)), maxMark);
    }

    private String getSummaryString(List<String> resultFile) {
        return resultFile.get(resultFile.size() - 1);
    }

    private Map<String, Integer> getResultMap(String summaryString) {
        Map<String, Integer> resultMap = new HashMap<>();
        List<String> fields = Arrays.asList(summaryString.split(","));

        fields.forEach(field -> {
                    String[] values = field.split(INTERNAL_FIELD_DELIMITER);
                    resultMap.put(values[0], Integer.parseInt(values[1]));
                });

        return resultMap;
    }

    private Integer getMark(Map<String, Integer> resultMap, Integer maxMark) {
        Integer totalTests = resultMap.get(FIELD_TOTAL_RUN);
        Integer failedTests = resultMap.get(FIELD_FAILED)
                + resultMap.get(FIELD_ERROR)
                + resultMap.get(FIELD_SKIPPED);

        return Math.round(((float)((totalTests - failedTests) / totalTests)) * maxMark);
    }
}
