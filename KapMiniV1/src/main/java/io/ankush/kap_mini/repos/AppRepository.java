package io.ankush.kap_mini.repos;

import io.ankush.kap_mini.domain.App;
import io.ankush.kap_mini.domain.SubDomain;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppRepository extends JpaRepository<App, UUID> {

    App findFirstBySubdomainId(SubDomain subDomain);

    List<App> findAllBySubdomainId(SubDomain subdomain);
}
