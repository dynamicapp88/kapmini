package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.domain.FieldData;
import io.ankush.kap_mini.domain.SubDomain;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.*;
import io.ankush.kap_mini.repos.SubDomainRepository;
import io.ankush.kap_mini.service.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping(value = "/api/custom", produces = MediaType.APPLICATION_JSON_VALUE)
public class MyController {

    @Autowired
    private SubDomainRepository subDomainRepository;
    // using UserId --> get all Application

     // 1.  appId --> all forms

    @PostMapping("/creater-user")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createuser(@RequestBody final JSONObject jsonObject) {
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

    @Autowired
    private UserService userService;

    @Autowired
    private  TemplateService templateService;
    @Autowired
    private AppService appService;
    @Autowired
    private FormService formService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private  FieldDataService fieldDataService;
    @Autowired
    private  RecordService recordService;
    // userId and template....
    // template : create application , and form , formFields and setData on it

    @PostMapping("/auto-template/{userId}/{templateId}")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createAutoTemplate(
            @PathVariable(name = "userId") final UUID userId,
            @PathVariable(name = "templateId") final UUID templateId
    ) {
        JSONObject response = new JSONObject();

        UserDTO userDTO=  userService.get(userId);
        if(userDTO == null){
            return new ResponseEntity<>("userId not found", HttpStatus.CREATED);
        }
        TemplateDTO templateDTO =  templateService.get(templateId);
        if(templateDTO == null){
            return new ResponseEntity<>("templateId not found", HttpStatus.CREATED);
        }

        JSONObject re = new JSONObject(templateDTO.getTemplateData());
        // template found and user found....

        // 1.create Application

        AppDTO appDTO = new AppDTO();
        appDTO.setName(re.optString("applicationName"));
        appDTO.setDescription(re.optString("applicationDescription"));
        appDTO.setIsActive(Isactive.ACTIVE);
        appDTO.setIsProduction(Isproduction.ENV);
        appDTO.setSubdomainId(userDTO.getSubdomainID());

        final UUID createdAppId = appService.create(appDTO);
        // appID --> createdAppId

        // now create form
        FormDTO formDTO = new FormDTO();
        formDTO.setName(re.optString("formName"));
        formDTO.setDescription(re.optString("formDescription"));
        formDTO.setAppID(createdAppId);

        final UUID createdFormId = formService.create(formDTO);
    //formFields

        JSONArray fieldAa = re.optJSONArray("formFields");
        Map<String,UUID> map = new HashMap<>();
        for(int i = 0 ; i < fieldAa.length() ; i++) {
            JSONObject fieldDetails = fieldAa.getJSONObject(i);
            // create fields form
            FieldDTO fieldDTO = new FieldDTO();
            fieldDTO.setFieldName(fieldDetails.optString("fieldName"));
            String fy = fieldDetails.optString("fieldType");
            if(FieldType.exists(fy)) {
                fieldDTO.setFieldType(FieldType.fromString(fy));
            }
            fieldDTO.setDefaultValue(fieldDetails.optString("defaultValue"));
            fieldDTO.setFieldId(createdFormId);

            String option =  fieldDetails.optString("options");
            if(option!= null && !option.equalsIgnoreCase("")) {
                fieldDTO.setValidationRules(option);
            }
            fieldDTO.setFormId(createdFormId);
            fieldDTO.setIsRequired(fieldDetails.optBoolean("isRequire", false));
            final UUID createdFieldId = fieldService.create(fieldDTO);
            map.put(fieldDetails.optString("fieldName"), createdFieldId);
        }


        // formFieldDatas
        JSONArray fieldData = re.optJSONArray("formData");

        for( int i = 0 ; i< fieldData.length() ; i++){

            //  Records Data
            RecordDTO recordDTO = new RecordDTO();
            recordDTO.setStatus("AUTO_TEMPLATE");
            recordDTO.setFormId(createdFormId);
            recordDTO.setCreatedBy(userDTO.getUserId());
            recordDTO.setUpdatedBy(userDTO.getUserId());
            final UUID createdRecordId = recordService.create(recordDTO);

            JSONObject fdata = fieldData.getJSONObject(i);
            Iterator<String> keys = fdata.keys();

            // Iterate over keys
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = fdata.get(key);
                System.out.println("Key: " + key + ", Value: " + value);
                UUID fId =  map.get(key);
                FieldDataDTO fieldDataDTO = new FieldDataDTO();
                fieldDataDTO.setValue(value.toString());
                fieldDataDTO.setFieldId(fId);
                fieldDataDTO.setRecordId(createdRecordId);
                final UUID createdFieldDataId = fieldDataService.create(fieldDataDTO);
            }




        }



        System.out.println(userId+"  n  "+templateId);

        response.put("appId",createdAppId);
        response.put("formId",createdFormId);

        String str = response.toString();
        return new ResponseEntity<>(str, HttpStatus.CREATED);
    }


}

