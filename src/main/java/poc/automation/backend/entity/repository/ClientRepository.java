package poc.automation.backend.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poc.automation.backend.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
