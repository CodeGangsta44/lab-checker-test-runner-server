package org.kpi.usb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class TestingService {
    private final String GITHUB_PREFIX = "git@github.com:";
    private final String OWNER = "labChecker";
    private final String GITHUB_POSTFIX = ".git";
    private final String TEST_AREA_DIRECTORY_NAME = "testing-area";
    private final String RESULT_FILE_NAME = "results.txt";

    private ReaderService readerService;
    private JavaResultParserService javaResultParserService;

    public TestingService(ReaderService readerService, JavaResultParserService javaResultParserService) {
        this.readerService = readerService;
        this.javaResultParserService = javaResultParserService;
    }

    public Integer runTest(String sourceRepoName, String testRepoName, String studentLogin, Integer studentVariant, Integer maxMark) {
        String sourceRepoUrl = GITHUB_PREFIX + studentLogin + "/" + sourceRepoName + GITHUB_POSTFIX;
        String testRepoUrl = GITHUB_PREFIX + OWNER + "/" + testRepoName + GITHUB_POSTFIX;
        String resultFileUri = TEST_AREA_DIRECTORY_NAME + "/" + studentLogin + "/" + RESULT_FILE_NAME;

        executeScript(sourceRepoUrl, testRepoUrl, studentVariant, studentLogin, sourceRepoName, testRepoName);
        return parseResult(readerService.readFile(resultFileUri), maxMark);
    }

    private void executeScript(String sourceRepoUrl, String testRepoUrl, int studentVariant, String studentLogin, String sourceRepoName, String testRepoName) {
        String cmd = "./shell/testing.sh"
                + " "
                + sourceRepoUrl
                + " "
                + testRepoUrl
                + " "
                + studentVariant
                + " "
                + studentLogin
                + " "
                + sourceRepoName
                + " "
                + testRepoName;

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
