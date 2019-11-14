package org.kpi.usb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class TestingService {
    private final String GITHUB_PREFIX = "git@github.com:";
    private final String OWNER = "CodeGangsta44/";
    private final String GITHUB_POSTFIX = ".git";
    private final String TEST_AREA_DIRECTORY_NAME = "testing-area/";
    private final String RESULT_FILE_NAME = "results.txt";

    private ReaderService readerService;
    private JavaResultParserService javaResultParserService;

    public TestingService(ReaderService readerService, JavaResultParserService javaResultParserService) {
        this.readerService = readerService;
        this.javaResultParserService = javaResultParserService;
    }

    public Integer runTest(String repoName, String studentLogin, Integer studentVariant, Integer maxMark) {
        String sourceRepo = GITHUB_PREFIX + studentLogin + "/" + repoName + GITHUB_POSTFIX;
        String testRepo = GITHUB_PREFIX + OWNER + repoName + "Test" + GITHUB_POSTFIX;
        String resultFileUri = TEST_AREA_DIRECTORY_NAME + studentLogin + "/" + RESULT_FILE_NAME;

        executeScript(sourceRepo, testRepo, studentVariant, studentLogin);
        return parseResult(readerService.readFile(resultFileUri), maxMark);
    }

    private void executeScript(String sourceRepo, String testRepo, int studentVariant, String studentLogin) {
        String cmd = "./src/main/resources/shell/testing.sh"
                + " "
                + sourceRepo
                + " "
                + testRepo
                + " "
                + String.format("%02d", studentVariant)
                + " "
                + studentLogin;

        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Integer parseResult(List<String> results, Integer maxMark) {
        return javaResultParserService.getResult(results, maxMark);
    }
}
