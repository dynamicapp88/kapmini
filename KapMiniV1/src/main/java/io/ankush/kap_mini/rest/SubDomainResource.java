package io.ankush.kap_mini.rest;

import io.ankush.kap_mini.model.SubDomainDTO;
import io.ankush.kap_mini.service.SubDomainService;
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
@RequestMapping(value = "/api/subDomains", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubDomainResource {

    private final SubDomainService subDomainService;

    public SubDomainResource(final SubDomainService subDomainService) {
        this.subDomainService = subDomainService;
    }

    @GetMapping
    public ResponseEntity<List<SubDomainDTO>> getAllSubDomains() {
        return ResponseEntity.ok(subDomainService.findAll());
    }

    @GetMapping("/{subdomain}")
    public ResponseEntity<SubDomainDTO> getSubDomain(
            @PathVariable(name = "subdomain") final UUID subdomain) {
        return ResponseEntity.ok(subDomainService.get(subdomain));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createSubDomain(
            @RequestBody @Valid final SubDomainDTO subDomainDTO) {
        final UUID createdSubdomain = subDomainService.create(subDomainDTO);
        return new ResponseEntity<>(createdSubdomain, HttpStatus.CREATED);
    }

    @PutMapping("/{subdomain}")
    public ResponseEntity<UUID> updateSubDomain(
            @PathVariable(name = "subdomain") final UUID subdomain,
            @RequestBody @Valid final SubDomainDTO subDomainDTO) {
        subDomainService.update(subdomain, subDomainDTO);
        return ResponseEntity.ok(subdomain);
    }

    @DeleteMapping("/{subdomain}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSubDomain(
            @PathVariable(name = "subdomain") final UUID subdomain) {
        final ReferencedWarning referencedWarning = subDomainService.getReferencedWarning(subdomain);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        subDomainService.delete(subdomain);
        return ResponseEntity.noContent().build();
    }

}
