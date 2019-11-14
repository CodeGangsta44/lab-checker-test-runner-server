package org.kpi.usb.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MainService {
    private final String GITHUB_PREFIX = "git@github.com:";
    private final String OWNER = "CodeGangsta44/";
    private final String GITHUB_POSTFIX = ".git";

    public void runTest(String repoName, String studentLogin, int studentVariant) {
        String sourceRepo = GITHUB_PREFIX + studentLogin + "/" + repoName + GITHUB_POSTFIX;
        String testRepo = GITHUB_PREFIX + OWNER + repoName + "Test" + GITHUB_POSTFIX;

        String homeDir = System.getenv("HOME");
        String cmd = "./src/main/resources/shell/testing.sh" + " " + sourceRepo + " " + testRepo;
        System.out.println(cmd);
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            System.out.println(p.getOutputStream());
            p.waitFor();
            System.out.println("FINISHED SUCCESSFULLY!");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
