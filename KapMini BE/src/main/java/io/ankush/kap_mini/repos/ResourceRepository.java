package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.Resource;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ResourceRepository extends JpaRepository<Resource, UUID> {
}
