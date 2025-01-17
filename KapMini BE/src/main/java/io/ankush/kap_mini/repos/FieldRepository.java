package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.Field;
import io.ankush.kap_mini.domain.Form;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FieldRepository extends JpaRepository<Field, UUID> {

    Field findFirstByFormId(Form form);

}
