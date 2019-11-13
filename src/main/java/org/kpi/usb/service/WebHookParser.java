package org.kpi.usb.service;

import org.json.JSONObject;
import org.kpi.usb.entity.LabRepo;
import org.kpi.usb.entity.PullRequest;
import org.kpi.usb.entity.PullRequestUser;

import java.util.HashMap;

public class WebHookParser {

    public static PullRequestUser getUserFromJSONWebHook(String json){
        JSONObject obj = new JSONObject(json);
        return PullRequestUser.builder()
                .login(obj.getJSONObject("sender").getString("login"))
                .id(obj.getJSONObject("sender").getLong("id"))
                .numberOfPoolRequestsForEachLab(new HashMap<String, Integer>())
                .build();
    }

    public static LabRepo getRepoFromJSONWebHook(String json){
        JSONObject obj = new JSONObject(json);
        return LabRepo.builder()
                .name(obj.getJSONObject("repository").getString("name"))
                .id(obj.getJSONObject("repository").getLong("id"))
                .language(obj.getJSONObject("repository").getString("language"))
                .forksCount(obj.getJSONObject("repository").getLong("forks_count"))
                .build();
    }

    public static PullRequest getRequestFromJSONWebHook(String json) {
        JSONObject obj = new JSONObject(json);
        return PullRequest.builder()
                .user(getUserFromJSONWebHook(json))
                .repo(getRepoFromJSONWebHook(json))
                .date(obj.getJSONObject("pull_request").getString("created_at"))
                .message(obj.getJSONObject("pull_request").getString("title"))
                .number(obj.getJSONObject("pull_request").getLong("number"))
                .build();
    }
}