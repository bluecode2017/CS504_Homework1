package demo.service.Impl;


import demo.domain.RunningInformation;
import demo.domain.RunningInformationRestRepository;
import demo.service.RunningInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RunningInformationServiceImpl implements RunningInformationService {


    private RunningInformationRestRepository runningInformationRestRepository;
    @Autowired
    public RunningInformationServiceImpl(RunningInformationRestRepository runningInformationRestRepository) {
        this.runningInformationRestRepository = runningInformationRestRepository;
    }
    @Override
    public List<RunningInformation> saveRunningInformation(List<RunningInformation> runningInformations) {
        return runningInformationRestRepository.save(runningInformations);
    }

    @Override
    public void deleteByRunningId(String runningId) {
        RunningInformation temp = runningInformationRestRepository.findByRunningId(runningId);
        runningInformationRestRepository.delete(temp);

    }

    @Override
    public Page<RunningInformation> findAll(Pageable pageable) {
         return runningInformationRestRepository.findAll(pageable);
    }

}
