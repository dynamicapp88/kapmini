package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.Template;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TemplateRepository extends JpaRepository<Template, UUID> {
}
