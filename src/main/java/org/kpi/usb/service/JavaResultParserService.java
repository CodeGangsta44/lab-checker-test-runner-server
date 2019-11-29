package org.kpi.usb.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JavaResultParserService {
    private static final String FIELD_TOTAL_RUN = "Tests run";
    private static final String FIELD_FAILED = "Failures";
    private static final String FIELD_ERROR = "Errors";
    private static final String FIELD_SKIPPED = "Skipped";
    private static final String INTERNAL_FIELD_DELIMITER = ": ";

    public Integer getResult(List<String> resultFile, Integer maxMark) {
        return getMark(getResultMap(getSummaryString(resultFile)), maxMark);
    }

    private String getSummaryString(List<String> resultFile) {
        return resultFile.get(resultFile.size() - 1);
    }

    private Map<String, Integer> getResultMap(String summaryString) {
        Map<String, Integer> resultMap = new HashMap<>();
        List<String> fields = Arrays.asList(summaryString.split(","));

        resultMap.put(FIELD_TOTAL_RUN, Integer.valueOf(fields
                .get(0)
                .split(INTERNAL_FIELD_DELIMITER)[1]));
        resultMap.put(FIELD_FAILED, Integer.valueOf(fields
                .get(1)
                .split(INTERNAL_FIELD_DELIMITER)[1]));
        resultMap.put(FIELD_ERROR, Integer.valueOf(fields
                .get(2)
                .split(INTERNAL_FIELD_DELIMITER)[1]));

        return resultMap;
    }

    private Integer getMark(Map<String, Integer> resultMap, Integer maxMark) {
        Integer totalTests = resultMap.get(FIELD_TOTAL_RUN);
        Integer failedTests = resultMap.get(FIELD_FAILED)
                + resultMap.get(FIELD_ERROR);

        return Math.round(((float) ((double) (totalTests - failedTests) / totalTests)) * maxMark);
    }
}
