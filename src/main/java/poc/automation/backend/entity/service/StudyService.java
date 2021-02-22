package poc.automation.backend.entity.service;

import org.springframework.stereotype.Service;
import poc.automation.backend.entity.Client;
import poc.automation.backend.entity.Study;
import poc.automation.backend.entity.repository.ClientRepository;
import poc.automation.backend.entity.repository.StudyRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudyService
{
    private static final Logger LOGGER = Logger.getLogger(StudyService.class.getName());
    private StudyRepository studyRepository;
    private ClientRepository clientRepository;

    public StudyService(StudyRepository studyRepository, ClientRepository clientRepository) {
        this.studyRepository = studyRepository;
        this.clientRepository = clientRepository;
    }

    public List<Study> findAll() {
        return studyRepository.findAll();
    }

    public List<Study> findAll(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return studyRepository.findAll();
        } else {
            return studyRepository.search(filterText);
        }
    }

    public long count() {
        return studyRepository.count();
    }

    public void delete(Study study) {
        studyRepository.delete(study);
    }

    public void save(Study study) {
        if (study == null) {
            LOGGER.log(Level.SEVERE,
                    "Study is null. Are you sure you have connected your form to the application?");
            return;
        }
        study.setCreatedOn(LocalDate.now());
        studyRepository.save(study);
    }

    @PostConstruct
    public void populateTestData() {
        if (clientRepository.count() == 0) {
            clientRepository.saveAll(
                    Stream.of("SDTM", "ADaM")
                            .map(Client::new)
                            .collect(Collectors.toList()));
        }

        if (studyRepository.count() == 0) {
            Random r = new Random(0);
            List<Client> studies = clientRepository.findAll();
            studyRepository.saveAll(
                    Stream.of("Natura 8980-KOLS", "LifePH UIUH-2190D", "NoblePH JSKS-OIUI9",
                            "QuickHealth SKSKS-91DS", "HealthSolutions 988-LO", "Medilife 12DDDD-092",
                            "Pharma OLUNDESD-999", "Ocuphire NNNMM-8989", "PharmaCO v10-200")
                            .map(name -> {
                                String[] split = name.split(" ");
                                Study study = new Study();
                                study.setStudyName(split[0]);
                                study.setID(split[1]);
                                study.setClient(studies.get(r.nextInt(studies.size())));
                                study.setCreatedOn(LocalDate.of(2019, 3, 29));
                                return study;
                            }).collect(Collectors.toList()));
        }
    }
}
