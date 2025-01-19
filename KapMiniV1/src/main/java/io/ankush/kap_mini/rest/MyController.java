package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.domain.SubDomain;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.AppDTO;
import io.ankush.kap_mini.repos.SubDomainRepository;
import io.ankush.kap_mini.service.CustomService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/custom", produces = MediaType.APPLICATION_JSON_VALUE)
public class MyController {

    @Autowired
    private SubDomainRepository subDomainRepository;
    // using UserId --> get all Application

     // 1.  appId --> all forms

    @PostMapping("/creater-user")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createuser(@RequestBody JSONObject jsonObject) {
        String domain = jsonObject.optString("subdomain");
        SubDomain sb = new SubDomain();
        sb.setName(domain);
        sb =  subDomainRepository.save(sb);


        User user = new User();
        user.setName(jsonObject.optString("name"));
        user.setEmail(jsonObject.optString("email"));
        user.setPassword(jsonObject.optString("password"));



        return new ResponseEntity<>("test", HttpStatus.CREATED);
    }

    // appId -> form -->


}

