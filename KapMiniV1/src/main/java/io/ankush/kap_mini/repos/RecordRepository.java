package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.Form;
import io.ankush.kap_mini.domain.Record;
import io.ankush.kap_mini.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecordRepository extends JpaRepository<Record, UUID> {

    Record findFirstByFormId(Form form);

    Record findFirstByCreatedBy(User user);

    Record findFirstByUpdatedBy(User user);

}
