package demo.service;

import demo.domain.RunningInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RunningInformationService {
    List<RunningInformation> saveRunningInformation(List<RunningInformation> runningInformations);

    void deleteByRunningId(String runningId);
    //void deleteByRunningId(List<RunningInformation> runningInformations, String runningId);

    Page<RunningInformation> findAll(Pageable pageable);


}
