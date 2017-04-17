package demo.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Service;

import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "runningInformations")
public interface RunningInformationRepository extends JpaRepository<RunningInformation, Long> {

    Page<RunningInformation> findAll(Pageable pageable);

    List<RunningInformation> findByRunningId(@Param("runningId") String runningId);

    void deleteByRunningId(@Param("runningId") String runningId);


}
