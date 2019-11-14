package org.kpi.usb.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.kpi.usb.entity.LabRepo;
import org.kpi.usb.entity.PullRequest;
import org.kpi.usb.entity.PullRequestUser;
import org.kpi.usb.entity.Result;
import org.springframework.stereotype.Service;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.HashMap;

@Service
public class JSONParser {

    public PullRequestUser getUserFromJSONWebHook(String json){
        JSONObject obj = new JSONObject(json);
        return PullRequestUser.builder()
                .login(obj.getJSONObject("sender").getString("login"))
                .id(obj.getJSONObject("sender").getLong("id"))
                .numberOfPullRequestsForEachLab(new HashMap<String, Integer>())
                .build();
    }

    public LabRepo getRepoFromJSONWebHook(String json){
        JSONObject obj = new JSONObject(json);
        return LabRepo.builder()
                .name(obj.getJSONObject("repository").getString("name"))
                .id(obj.getJSONObject("repository").getLong("id"))
                .language(obj.getJSONObject("repository").getString("language"))
                .forksCount(obj.getJSONObject("repository").getLong("forks_count"))
                .build();
    }

    public PullRequest getRequestFromJSONWebHook(String json) {
        JSONObject obj = new JSONObject(json);
        return PullRequest.builder()
                .user(getUserFromJSONWebHook(json))
                .repo(getRepoFromJSONWebHook(json))
                .date(obj.getJSONObject("pull_request").getString("created_at"))
                .message(obj.getJSONObject("pull_request").getString("title"))
                .number(obj.getJSONObject("pull_request").getLong("number"))
                .build();
    }

    public String createResultJSONString (Result result){
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(result);
        } catch (IOException e){
            e.printStackTrace();
        }
        return jsonInString;
    }
}