package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.domain.Collaborator;
import io.ankush.kap_mini.domain.SubDomain;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.SubDomainDTO;
import io.ankush.kap_mini.repos.AppRepository;
import io.ankush.kap_mini.repos.CollaboratorRepository;
import io.ankush.kap_mini.repos.SubDomainRepository;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.util.NotFoundException;
import io.ankush.kap_mini.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SubDomainService {

    private final SubDomainRepository subDomainRepository;
    private final UserRepository userRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final AppRepository appRepository;

    public SubDomainService(final SubDomainRepository subDomainRepository,
            final UserRepository userRepository,
            final CollaboratorRepository collaboratorRepository,
            final AppRepository appRepository) {
        this.subDomainRepository = subDomainRepository;
        this.userRepository = userRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.appRepository = appRepository;
    }

    public List<SubDomainDTO> findAll() {
        final List<SubDomain> subDomains = subDomainRepository.findAll(Sort.by("subdomain"));
        return subDomains.stream()
                .map(subDomain -> mapToDTO(subDomain, new SubDomainDTO()))
                .toList();
    }

    public SubDomainDTO get(final UUID subdomain) {
        return subDomainRepository.findById(subdomain)
                .map(subDomain -> mapToDTO(subDomain, new SubDomainDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final SubDomainDTO subDomainDTO) {
        final SubDomain subDomain = new SubDomain();
        mapToEntity(subDomainDTO, subDomain);
        return subDomainRepository.save(subDomain).getSubdomain();
    }

    public void update(final UUID subdomain, final SubDomainDTO subDomainDTO) {
        final SubDomain subDomain = subDomainRepository.findById(subdomain)
                .orElseThrow(NotFoundException::new);
        mapToEntity(subDomainDTO, subDomain);
        subDomainRepository.save(subDomain);
    }

    public void delete(final UUID subdomain) {
        subDomainRepository.deleteById(subdomain);
    }

    private SubDomainDTO mapToDTO(final SubDomain subDomain, final SubDomainDTO subDomainDTO) {
        subDomainDTO.setSubdomain(subDomain.getSubdomain());
        subDomainDTO.setName(subDomain.getName());
        subDomainDTO.setDeleteAt(subDomain.getDeleteAt());
        return subDomainDTO;
    }

    private SubDomain mapToEntity(final SubDomainDTO subDomainDTO, final SubDomain subDomain) {
        subDomain.setName(subDomainDTO.getName());
        subDomain.setDeleteAt(subDomainDTO.getDeleteAt());
        return subDomain;
    }

    public ReferencedWarning getReferencedWarning(final UUID subdomain) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final SubDomain subDomain = subDomainRepository.findById(subdomain)
                .orElseThrow(NotFoundException::new);
        final User subdomainIDUser = userRepository.findFirstBySubdomainID(subDomain);
        if (subdomainIDUser != null) {
            referencedWarning.setKey("subDomain.user.subdomainID.referenced");
            referencedWarning.addParam(subdomainIDUser.getUserId());
            return referencedWarning;
        }
        final Collaborator subdomainIdCollaborator = collaboratorRepository.findFirstBySubdomainId(subDomain);
        if (subdomainIdCollaborator != null) {
            referencedWarning.setKey("subDomain.collaborator.subdomainId.referenced");
            referencedWarning.addParam(subdomainIdCollaborator.getCollaboratorID());
            return referencedWarning;
        }
        final App subdomainIdApp = appRepository.findFirstBySubdomainId(subDomain);
        if (subdomainIdApp != null) {
            referencedWarning.setKey("subDomain.app.subdomainId.referenced");
            referencedWarning.addParam(subdomainIdApp.getAppId());
            return referencedWarning;
        }
        return null;
    }

}
