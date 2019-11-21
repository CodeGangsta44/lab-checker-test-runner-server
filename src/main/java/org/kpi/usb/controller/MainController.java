package org.kpi.usb.controller;

import org.kpi.usb.entity.PullRequest;
import org.kpi.usb.entity.Repository;
import org.kpi.usb.entity.Result;
import org.kpi.usb.entity.Student;
import org.kpi.usb.service.*;
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

    @PostMapping
    public void receiveWebHook(@RequestBody String content) {
        Repository repository = githubWebHookService.getRepositoryFromJSON(content);
        Student student = githubWebHookService.getStudentFromJSON(content);
        PullRequest pullRequest = githubWebHookService.getPullRequestFromJSON(content);

        Optional<Integer> studentVariantOptional = userService.getUserVariantByGithubID(student.getId());
        Optional<Integer> maxMarkForLab = labService.getMaxMarkForLab(repository.getName());

        int studentVariant = studentVariantOptional.orElseThrow();
        int maxMark = maxMarkForLab.orElseThrow();

        final int mark = testingService.runTest(repository.getName(),
                student.getLogin(),
                studentVariant,
                maxMark);

        Result result = Result.builder()
                .studentGithubLogin(student.getLogin())
                .studentGithubID(student.getId())
                .repositoryName(repository.getName())
                .mark(mark)
                .variant(studentVariant)
                .language(repository.getLanguage())
                .updatedDate(pullRequest.getUpdatedDate())
                .build();

        resultService.sendResultToPersistenceServer(result);
    }

}

