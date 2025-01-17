package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.Collaborator;
import io.ankush.kap_mini.domain.SubDomain;
import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.model.CollaboratorDTO;
import io.ankush.kap_mini.repos.CollaboratorRepository;
import io.ankush.kap_mini.repos.SubDomainRepository;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final UserRepository userRepository;
    private final SubDomainRepository subDomainRepository;

    public CollaboratorService(final CollaboratorRepository collaboratorRepository,
            final UserRepository userRepository, final SubDomainRepository subDomainRepository) {
        this.collaboratorRepository = collaboratorRepository;
        this.userRepository = userRepository;
        this.subDomainRepository = subDomainRepository;
    }

    public List<CollaboratorDTO> findAll() {
        final List<Collaborator> collaborators = collaboratorRepository.findAll(Sort.by("collaboratorID"));
        return collaborators.stream()
                .map(collaborator -> mapToDTO(collaborator, new CollaboratorDTO()))
                .toList();
    }

    public CollaboratorDTO get(final UUID collaboratorID) {
        return collaboratorRepository.findById(collaboratorID)
                .map(collaborator -> mapToDTO(collaborator, new CollaboratorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CollaboratorDTO collaboratorDTO) {
        final Collaborator collaborator = new Collaborator();
        mapToEntity(collaboratorDTO, collaborator);
        return collaboratorRepository.save(collaborator).getCollaboratorID();
    }

    public void update(final UUID collaboratorID, final CollaboratorDTO collaboratorDTO) {
        final Collaborator collaborator = collaboratorRepository.findById(collaboratorID)
                .orElseThrow(NotFoundException::new);
        mapToEntity(collaboratorDTO, collaborator);
        collaboratorRepository.save(collaborator);
    }

    public void delete(final UUID collaboratorID) {
        collaboratorRepository.deleteById(collaboratorID);
    }

    private CollaboratorDTO mapToDTO(final Collaborator collaborator,
            final CollaboratorDTO collaboratorDTO) {
        collaboratorDTO.setCollaboratorID(collaborator.getCollaboratorID());
        collaboratorDTO.setDeleteAt(collaborator.getDeleteAt());
        collaboratorDTO.setUserID(collaborator.getUserID() == null ? null : collaborator.getUserID().getUserId());
        collaboratorDTO.setSubdomainId(collaborator.getSubdomainId() == null ? null : collaborator.getSubdomainId().getSubdomain());
        return collaboratorDTO;
    }

    private Collaborator mapToEntity(final CollaboratorDTO collaboratorDTO,
            final Collaborator collaborator) {
        collaborator.setDeleteAt(collaboratorDTO.getDeleteAt());
        final User userID = collaboratorDTO.getUserID() == null ? null : userRepository.findById(collaboratorDTO.getUserID())
                .orElseThrow(() -> new NotFoundException("userID not found"));
        collaborator.setUserID(userID);
        final SubDomain subdomainId = collaboratorDTO.getSubdomainId() == null ? null : subDomainRepository.findById(collaboratorDTO.getSubdomainId())
                .orElseThrow(() -> new NotFoundException("subdomainId not found"));
        collaborator.setSubdomainId(subdomainId);
        return collaborator;
    }

}
