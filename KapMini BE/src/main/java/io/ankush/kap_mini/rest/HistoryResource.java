package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.HistoryDTO;
import io.ankush.kap_mini.service.HistoryService;
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
@RequestMapping(value = "/api/histories", produces = MediaType.APPLICATION_JSON_VALUE)
public class HistoryResource {

    private final HistoryService historyService;

    public HistoryResource(final HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public ResponseEntity<List<HistoryDTO>> getAllHistories() {
        return ResponseEntity.ok(historyService.findAll());
    }

    @GetMapping("/{historyID}")
    public ResponseEntity<HistoryDTO> getHistory(
            @PathVariable(name = "historyID") final UUID historyID) {
        return ResponseEntity.ok(historyService.get(historyID));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createHistory(@RequestBody @Valid final HistoryDTO historyDTO) {
        final UUID createdHistoryID = historyService.create(historyDTO);
        return new ResponseEntity<>(createdHistoryID, HttpStatus.CREATED);
    }

    @PutMapping("/{historyID}")
    public ResponseEntity<UUID> updateHistory(
            @PathVariable(name = "historyID") final UUID historyID,
            @RequestBody @Valid final HistoryDTO historyDTO) {
        historyService.update(historyID, historyDTO);
        return ResponseEntity.ok(historyID);
    }

    @DeleteMapping("/{historyID}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteHistory(
            @PathVariable(name = "historyID") final UUID historyID) {
        historyService.delete(historyID);
        return ResponseEntity.noContent().build();
    }

}
