package demo.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RunningInformationRepository extends JpaRepository<RunningInformation,Long> {

    List<RunningInformation> save(List<RunningInformation> runningInformations);

    Page<RunningInformation> findAll(Pageable pageable);

    RunningInformation findByRunningId(@Param("runningId") String runningId);

    void deleteByRunningId(@Param("runningId") String runningId);
}
