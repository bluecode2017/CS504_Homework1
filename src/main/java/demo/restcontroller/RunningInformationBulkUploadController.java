package demo.restcontroller;

import demo.domain.RunningInformation;
import demo.service.RunningInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@EnableAutoConfiguration
@RestController
public class RunningInformationBulkUploadController {
    @Autowired
    private RunningInformationService runningInformationService;

    @RequestMapping(value = "/bulkUpload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void upload(@RequestBody List<RunningInformation> runningInformations) {
        runningInformationService.saveRunningInformation(runningInformations);
    }

    @RequestMapping(value = "/deleteByRunningId/{runningId}", method = RequestMethod.DELETE)
    public void deleteByRunningId(@PathVariable("runningId") String runningId) {
        runningInformationService.deleteByRunningId(runningId);
    }

    @RequestMapping(value="/findAll", method = RequestMethod.GET)
    public Page<RunningInformation> findAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                            @RequestParam(name = "size",defaultValue = "2") Integer size){
        Sort sort = new Sort(Sort.Direction.DESC,"healthWarningLevel");
        Pageable pageable = new PageRequest(page,size,sort);
        return runningInformationService.findAll(pageable);
    }
    @RequestMapping(value = "/purge", method = RequestMethod.DELETE)
    public void purge() {
        this.runningInformationService.deleteAll();
    }

}
