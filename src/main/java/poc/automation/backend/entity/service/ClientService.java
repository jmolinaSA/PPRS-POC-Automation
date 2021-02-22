package poc.automation.backend.entity.service;

import org.springframework.stereotype.Service;
import poc.automation.backend.entity.Client;
import poc.automation.backend.entity.repository.ClientRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Map<String, Integer> getStats() {
        HashMap<String, Integer> stats = new HashMap<>();
        findAll().forEach(client ->
                stats.put(client.getName(), client.getStudies().size()));
        return stats;
    }
}
