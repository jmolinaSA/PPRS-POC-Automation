package poc.automation.backend.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poc.automation.backend.entity.Study;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long>
{
    @Query("select s from Study s " +
            "where lower(s.studyName) like lower(concat('%', :searchTerm, '%'))")
    List<Study> search(@Param("searchTerm") String searchTerm);
}
