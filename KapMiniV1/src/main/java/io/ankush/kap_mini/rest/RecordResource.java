package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.RecordDTO;
import io.ankush.kap_mini.service.RecordService;
import io.ankush.kap_mini.util.ReferencedException;
import io.ankush.kap_mini.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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
@RequestMapping(value = "/api/records", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecordResource {

    private final RecordService recordService;

    public RecordResource(final RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    public ResponseEntity<List<RecordDTO>> getAllRecords() {
        return ResponseEntity.ok(recordService.findAll());
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<RecordDTO> getRecord(
            @PathVariable(name = "recordId") final UUID recordId) {
        return ResponseEntity.ok(recordService.get(recordId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createRecord(@RequestBody @Valid final RecordDTO recordDTO) {
        final UUID createdRecordId = recordService.create(recordDTO);
        return new ResponseEntity<>(createdRecordId, HttpStatus.CREATED);
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<UUID> updateRecord(@PathVariable(name = "recordId") final UUID recordId,
            @RequestBody @Valid final RecordDTO recordDTO) {
        recordService.update(recordId, recordDTO);
        return ResponseEntity.ok(recordId);
    }

    @DeleteMapping("/{recordId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRecord(@PathVariable(name = "recordId") final UUID recordId) {
        final ReferencedWarning referencedWarning = recordService.getReferencedWarning(recordId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        recordService.delete(recordId);
        return ResponseEntity.noContent().build();
    }

}
