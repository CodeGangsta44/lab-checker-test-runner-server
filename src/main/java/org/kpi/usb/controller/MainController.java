package org.kpi.usb.controller;

import org.kpi.usb.entity.Result;
import org.kpi.usb.service.JSONParser;
import org.kpi.usb.service.TestingService;
import org.kpi.usb.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class MainController {

    private TestingService testingService;
    private UserService userService;
    private JSONParser JSONParser;

    public MainController(TestingService testingService,
                          UserService userService,
                          JSONParser JSONParser) {
        this.testingService = testingService;
        this.userService = userService;
        this.JSONParser = JSONParser;
    }

    //Todo refactor method
    @PostMapping
    public void postMethod(@RequestBody String body) {

        String repoName = JSONParser.getRepoFromJSONWebHook(body).getName();
        String language = JSONParser.getRepoFromJSONWebHook(body).getLanguage();
        String login = JSONParser.getUserFromJSONWebHook(body).getLogin();
        Long studentID = JSONParser.getUserFromJSONWebHook(body).getId();
        String date = JSONParser.getRequestFromJSONWebHook(body).getDate();

        Optional<Integer> studentVariantOptional = userService.getUserVariantByGithubID(studentID);
        //TODO Check optional before get :)
        int studentVariant = studentVariantOptional.get();

        testingService.runTest(repoName, login, studentVariant, 5);

        //Todo add getting number of passed tests
        Long testsPassedNumber = 0L;

        Result result = Result.builder()
                .result(testsPassedNumber)
                .studentLogin(login)
                .labName(repoName)
                .language(language)
                .commitDate(date)
                .studentID(studentID)
                .variant((long) studentVariant)
                .build();

        //Todo add sending resultJSONString
        String jsonString = JSONParser.createResultJSONString(result);

    }

}

