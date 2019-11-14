package org.kpi.usb.controller;

import org.kpi.usb.entity.PullRequest;
import org.kpi.usb.entity.Repository;
import org.kpi.usb.entity.Result;
import org.kpi.usb.entity.Student;
import org.kpi.usb.service.GithubWebHookService;
import org.kpi.usb.service.MainService;
import org.kpi.usb.service.ResultService;
import org.kpi.usb.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class MainController {

    private MainService mainService;
    private UserService userService;
    private ResultService resultService;
    private GithubWebHookService githubWebHookService;

    public MainController(MainService mainService,
                          UserService userService,
                          ResultService resultService,
                          GithubWebHookService githubWebHookService) {
        this.mainService = mainService;
        this.userService = userService;
        this.resultService = resultService;
        this.githubWebHookService = githubWebHookService;
    }

    @PostMapping
    public void receiveWebHook(@RequestBody String content) {
        Repository repository = githubWebHookService.getRepositoryFromJSON(content);
        Student student = githubWebHookService.getStudentFromJSON(content);
        PullRequest pullRequest = githubWebHookService.getPullRequestFromJSON(content);

        Optional<Integer> studentVariantOptional = userService.getUserVariantByGithubID(student.getId());
        //TODO Check optional before get :)
        int studentVariant = studentVariantOptional.get();

        mainService.runTest(repository.getName(), student.getLogin(), studentVariant);

        //Todo add getting number of passed tests
        final int TEMP_MARK = 10;

        Result result = Result.builder()
                .studentGithubLogin(student.getLogin())
                .studentGithubID(student.getId())
                .repositoryName(repository.getName())
                .mark(TEMP_MARK)
                .variant(studentVariant)
                .language(repository.getLanguage())
                .updatedDate(pullRequest.getUpdatedDate())
                .build();

        resultService.sendResultToPersistenceServer(result);
    }

}

