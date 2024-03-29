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
    private NotificationService notificationService;

    public MainController(TestingService testingService,
                          UserService userService,
                          ResultService resultService,
                          GithubWebHookService githubWebHookService,
                          LabService labService,
                          NotificationService notificationService) {
        this.testingService = testingService;
        this.userService = userService;
        this.resultService = resultService;
        this.githubWebHookService = githubWebHookService;
        this.labService = labService;
        this.notificationService = notificationService;
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

        checkPullRequestAction(pullRequest);

        log.info("Start receiving variant for student");
        Optional<Integer> studentVariantOptional = userService.getUserVariantByGithubIDAndLabRepo(student.getId(), repository.getName());
        log.info("Start receiving maximal mark for lab for student");
        Optional<Integer> maxMarkForLab = labService.getMaxMarkForLab(repository.getName());
        log.info("Start receiving test repo name for lab");
        Optional<String> testRepoNameForLab = labService.getTestRepoNameForLab(repository.getName());

        int studentVariant = studentVariantOptional.orElseThrow();
        log.info("Student variant: {}", studentVariant);
        int maxMark = maxMarkForLab.orElseThrow();
        log.info("Max mark for lab: {}", maxMark);
        String testRepoName = testRepoNameForLab.orElseThrow();
        log.info("Test repo name for lab: {}", testRepoName);

        notificationService.notifyAboutStartOfBuild(repository.getName(), student.getId());

        log.info("Start testing lab {} for student {}", repository.getName(), student.getLogin());
        final int mark = testingService.runTest(repository.getName(),
                testRepoName,
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

    private void checkPullRequestAction(PullRequest pullRequest) throws IllegalStateException {
        if (!isOpened(pullRequest) && !isReopened(pullRequest)) {
            log.warn("Pull request has illegal action: {}", pullRequest.getAction());
            throw new IllegalStateException("Should trigger only on opened pull request");
        }
    }

    private boolean isReopened(PullRequest pullRequest) {
        return pullRequest.getAction().equals("reopened");
    }

    private boolean isOpened(PullRequest pullRequest) {
        return pullRequest.getAction().equals("opened");
    }

}

