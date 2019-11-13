package org.kpi.usb.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kpi.usb.service.MainService;
import org.kpi.usb.service.WebHookParser;
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
    private WebHookParser webHookParser;

    @Autowired
    public MainController(MainService mainService, WebHookParser webHookParser) {
        this.webHookParser = webHookParser;
        this.mainService = mainService;
    }

    @PostMapping
    public void postMethod(@RequestBody String body) {

        String repoName = webHookParser.getRepoFromJSONWebHook(body).getName();
        String login = webHookParser.getUserFromJSONWebHook(body).getLogin();

        mainService.runTest(repoName,login);

    }
}
