package org.kpi.usb.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kpi.usb.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/")
public class MainController {

    private MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @PostMapping
    public void postMethod(@RequestBody String body) {
        System.out.println("I am here");
        System.out.println(body);

        try {
            Map<String, Object> requestMap = new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {});

            Map<String, Object> pullRequestMap = ((Map<String, Object>)(requestMap.get("pull_request")));
            Map<String, Object> userMap = ((Map<String, Object>)(pullRequestMap.get("user")));
            Map<String, Object> repositoryMap = ((Map<String, Object>)(requestMap.get("repository")));

            String login = (String)(userMap.get("login"));
            String repoName = (String)(repositoryMap.get("name"));

            System.out.println(requestMap);
            System.out.println(login);
            System.out.println(repoName);

            mainService.runTest(repoName, login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
