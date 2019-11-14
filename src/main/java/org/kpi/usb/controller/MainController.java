package org.kpi.usb.controller;

import org.kpi.usb.entity.Result;
import org.kpi.usb.service.MainService;
import org.kpi.usb.service.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

    private MainService mainService;
    private JSONParser JSONParser;

    @Autowired
    public MainController(MainService mainService, JSONParser JSONParser) {
        this.JSONParser = JSONParser;
        this.mainService = mainService;
    }

    //Todo refactor method
    @PostMapping
    public void postMethod(@RequestBody String body) {

        //TODO Add getting variant
        Long variant = 0L;

        String repoName = JSONParser.getRepoFromJSONWebHook(body).getName();
        String login = JSONParser.getUserFromJSONWebHook(body).getLogin();
        Long studentID = JSONParser.getUserFromJSONWebHook(body).getId();
        String date = JSONParser.getRequestFromJSONWebHook(body).getDate();

        mainService.runTest(repoName, login);

        //Todo add getting number of passed tests
        Long testsPassedNumber = 0L;

        Result result = Result.builder()
                .result(testsPassedNumber)
                .login(login)
                .date(date)
                .studentID(studentID)
                .variant(variant)
                .build();

        //Todo add sending resultJSONString
        String jsonString = JSONParser.createResultJSONString(result);

    }
}
