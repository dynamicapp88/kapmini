package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.model.AppDTO;
import io.ankush.kap_mini.service.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/custom", produces = MediaType.APPLICATION_JSON_VALUE)
public class MyController {


    // using UserId --> get all Application

     // 1.  appId --> all forms



    // appId -> form -->


}

