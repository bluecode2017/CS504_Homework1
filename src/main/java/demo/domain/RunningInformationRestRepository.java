package demo.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;


@RepositoryRestResource( path = "runningInformation", collectionResourceRel = "runningInformations")
public interface RunningInformationRestRepository extends JpaRepository<RunningInformation,Long> {
    @RestResource(path = "runningId", rel = "by-runningId")
    Page<RunningInformation> findAll( Pageable pageable);

    RunningInformation findByRunningId(@Param("runningId") String runningId);
    List<RunningInformation> save(List<RunningInformation> runningInformations);


    void deleteByRunningId(@Param("runningId") String runningId);
    //void deleteByRunningId(List<RunningInformation> runningInformations, String runningId);


}
