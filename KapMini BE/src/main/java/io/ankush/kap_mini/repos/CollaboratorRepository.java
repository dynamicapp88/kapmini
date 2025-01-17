package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.Collaborator;
import io.ankush.kap_mini.domain.SubDomain;
import io.ankush.kap_mini.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CollaboratorRepository extends JpaRepository<Collaborator, UUID> {

    Collaborator findFirstByUserID(User user);

    Collaborator findFirstBySubdomainId(SubDomain subDomain);

}
