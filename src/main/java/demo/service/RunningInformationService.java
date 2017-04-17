package demo.service;

import demo.domain.RunningInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RunningInformationService {
    public List<RunningInformation> saveRunningInformation(List<RunningInformation> runningInformations);

    public void deleteByRunningId(String runningId);

    public Page<RunningInformation> findAll(Pageable pageable);
    public void deleteAll();

    public void saveRandomOne(RunningInformation runningInformation);
}
