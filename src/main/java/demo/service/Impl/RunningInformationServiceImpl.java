package demo.service.Impl;


import demo.domain.RunningInformation;
import demo.domain.RunningInformationRepository;
import demo.service.RunningInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RunningInformationServiceImpl implements RunningInformationService {


    private RunningInformationRepository runningInformationRepository;
    @Autowired
    public RunningInformationServiceImpl(RunningInformationRepository runningInformationRepository) {
        this.runningInformationRepository = runningInformationRepository;
    }
    @Override
    public List<RunningInformation> saveRunningInformation(List<RunningInformation> runningInformations) {
        return runningInformationRepository.save(runningInformations);
    }

    @Override
    public void deleteByRunningId(String runningId) {

        List<RunningInformation> runningInformationList = new ArrayList<RunningInformation>();
        runningInformationList=runningInformationRepository.findByRunningId(runningId);
        for(RunningInformation temp: runningInformationList) {

                runningInformationRepository.delete(temp);

        }
    }

    @Override
    public Page<RunningInformation> findAll(Pageable pageable) {
         return runningInformationRepository.findAll(pageable);
    }

    @Override
    public void deleteAll(){
         runningInformationRepository.deleteAll();
    }

}
