package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.Field;
import io.ankush.kap_mini.domain.FieldData;
import io.ankush.kap_mini.domain.Record;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FieldDataRepository extends JpaRepository<FieldData, UUID> {

    FieldData findFirstByFieldId(Field field);

    FieldData findFirstByRecordId(Record record);

}
