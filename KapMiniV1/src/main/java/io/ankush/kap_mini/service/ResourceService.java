package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.Resource;
import io.ankush.kap_mini.model.ResourceDTO;
import io.ankush.kap_mini.repos.ResourceRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(final ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<ResourceDTO> findAll() {
        final List<Resource> resources = resourceRepository.findAll(Sort.by("resourceId"));
        return resources.stream()
                .map(resource -> mapToDTO(resource, new ResourceDTO()))
                .toList();
    }

    public ResourceDTO get(final UUID resourceId) {
        return resourceRepository.findById(resourceId)
                .map(resource -> mapToDTO(resource, new ResourceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ResourceDTO resourceDTO) {
        final Resource resource = new Resource();
        mapToEntity(resourceDTO, resource);
        return resourceRepository.save(resource).getResourceId();
    }

    public void update(final UUID resourceId, final ResourceDTO resourceDTO) {
        final Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(resourceDTO, resource);
        resourceRepository.save(resource);
    }

    public void delete(final UUID resourceId) {
        resourceRepository.deleteById(resourceId);
    }

    private ResourceDTO mapToDTO(final Resource resource, final ResourceDTO resourceDTO) {
        resourceDTO.setResourceId(resource.getResourceId());
        resourceDTO.setName(resource.getName());
        resourceDTO.setDescription(resource.getDescription());
        return resourceDTO;
    }

    private Resource mapToEntity(final ResourceDTO resourceDTO, final Resource resource) {
        resource.setName(resourceDTO.getName());
        resource.setDescription(resourceDTO.getDescription());
        return resource;
    }

}
