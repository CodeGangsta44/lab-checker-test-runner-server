package org.kpi.usb.controller;

import lombok.extern.slf4j.Slf4j;
import org.kpi.usb.entity.PullRequest;
import org.kpi.usb.entity.Repository;
import org.kpi.usb.entity.Result;
import org.kpi.usb.entity.Student;
import org.kpi.usb.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/")
@Slf4j
public class MainController {

    private TestingService testingService;
    private UserService userService;
    private ResultService resultService;
    private GithubWebHookService githubWebHookService;
    private LabService labService;

    public MainController(TestingService testingService,
                          UserService userService,
                          ResultService resultService,
                          GithubWebHookService githubWebHookService,
                          LabService labService) {
        this.testingService = testingService;
        this.userService = userService;
        this.resultService = resultService;
        this.githubWebHookService = githubWebHookService;
        this.labService = labService;
    }

    @GetMapping
    public String getHelloWorld() {
        return "Hello from labchecker!";
    }

    @PostMapping
    public void receiveWebHook(@RequestBody String content) {
        log.info("Start parsing entities from json webhook");
        Repository repository = githubWebHookService.getRepositoryFromJSON(content);
        Student student = githubWebHookService.getStudentFromJSON(content);
        PullRequest pullRequest = githubWebHookService.getPullRequestFromJSON(content);
        log.info("Entities parsed successfully");
        log.info("Current student login: {}", student.getLogin());

        log.info("Start receiving variant for student");
        Optional<Integer> studentVariantOptional = userService.getUserVariantByGithubIDAndLabRepo(student.getId(), repository.getName());
        log.info("Start receiving maximal mark for lab for student");
        Optional<Integer> maxMarkForLab = labService.getMaxMarkForLab(repository.getName());

        int studentVariant = studentVariantOptional.orElseThrow();
        log.info("Student variant: {}", studentVariant);
        int maxMark = maxMarkForLab.orElseThrow();
        log.info("Max mark for lab: {}", maxMark);


        log.info("Start testing lab {} for student {}", repository.getName(), student.getLogin());
        final int mark = testingService.runTest(repository.getName(),
                student.getLogin(),
                studentVariant,
                maxMark);
        log.info("Lab was tested, student mark is - {}", mark);


        log.info("Building lab result");
        Result result = Result.builder()
                .studentGithubLogin(student.getLogin())
                .studentGithubID(student.getId())
                .repositoryName(repository.getName())
                .mark(mark)
                .variant(studentVariant)
                .language(repository.getLanguage())
                .updatedDate(pullRequest.getUpdatedDate())
                .build();
        log.info("Student result: {}", result);

        log.info("Sending result to persistence server");
        resultService.sendResultToPersistenceServer(result);
    }

}

