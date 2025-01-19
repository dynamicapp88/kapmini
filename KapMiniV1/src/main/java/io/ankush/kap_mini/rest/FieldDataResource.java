package io.ankush.kap_mini.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.ankush.kap_mini.domain.FieldData;
import io.ankush.kap_mini.model.FieldDTO;
import io.ankush.kap_mini.model.FieldDataDTO;
import io.ankush.kap_mini.model.FormDTO;
import io.ankush.kap_mini.service.FieldDataService;
import io.ankush.kap_mini.service.FieldService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/fieldDatas", produces = MediaType.APPLICATION_JSON_VALUE)
public class FieldDataResource {

    private final FieldDataService fieldDataService;

    public FieldDataResource(final FieldDataService fieldDataService) {
        this.fieldDataService = fieldDataService;
    }

    @GetMapping
    public ResponseEntity<List<FieldDataDTO>> getAllFieldDatas() {
        return ResponseEntity.ok(fieldDataService.findAll());
    }

    @GetMapping("/{fieldDataId}")
    public ResponseEntity<FieldDataDTO> getFieldData(
            @PathVariable(name = "fieldDataId") final UUID fieldDataId) {
        return ResponseEntity.ok(fieldDataService.get(fieldDataId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createFieldData(
            @RequestBody @Valid final FieldDataDTO fieldDataDTO) {
        final UUID createdFieldDataId = fieldDataService.create(fieldDataDTO);
        return new ResponseEntity<>(createdFieldDataId, HttpStatus.CREATED);
    }

    @PostMapping("/addList")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createFieldDatas(
            @RequestBody @Valid final List<FieldDataDTO> fieldDataDTOList) {
        for (FieldDataDTO ff : fieldDataDTOList) {
            fieldDataService.create(ff);
        }
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @PutMapping("/{fieldDataId}")
    public ResponseEntity<UUID> updateFieldData(
            @PathVariable(name = "fieldDataId") final UUID fieldDataId,
            @RequestBody @Valid final FieldDataDTO fieldDataDTO) {
        fieldDataService.update(fieldDataId, fieldDataDTO);
        return ResponseEntity.ok(fieldDataId);
    }

    @DeleteMapping("/{fieldDataId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteFieldData(
            @PathVariable(name = "fieldDataId") final UUID fieldDataId) {
        fieldDataService.delete(fieldDataId);
        return ResponseEntity.noContent().build();
    }


    ///
    @Autowired
    private FieldService fieldService;



    @GetMapping("getAllFieldDataBasedOnFormId/{formId}")
    public List<FieldDataDTO> getALlFieldDataBasedOnFormId(
            @PathVariable(name = "formId") final UUID formId) {
        List<FieldDTO> fieldList = fieldService.getListOfAllFieldBasedOnFormId(formId);
        // formId
        //list of fieldIds
        // fetch all data
        List<FieldDataDTO> lss = new ArrayList<>();
        for (FieldDTO ff : fieldList) {
            List<FieldDataDTO> a = fieldDataService.getAllDataOfField(ff.getFieldId());
            for (FieldDataDTO aaa : a) {
                lss.add(aaa);
            }
        }
        return lss;

    }
}
