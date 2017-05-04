package demo.restcontroller;

import demo.domain.RunningInformation;
import demo.domain.UserInfo;
import demo.service.RunningInformationService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@EnableAutoConfiguration
@RestController
public class RunningInformationAnalysisController {

    private String runningId = "runningId";
    private String totalRunningTime = "totalRunningTime";
    private String heartRate = "heartRate";
    private String userId = "userId";
    private String userName = "userName";
    private String userAddress = "userAddress";
    private String healthWarningLevel = "healthWarningLevel";
    private final String kDefaultPage = "0";
    private final String kDefaultItemPerPage = "2";

    @Autowired
    private RunningInformationService runningInformationService;

    @RequestMapping(value = "/runninginformations", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void bulkUpload(@RequestBody List<RunningInformation> runningInformations) {
        runningInformationService.saveRunningInformation(runningInformations);
    }

    @RequestMapping(value = "/runninginformations/{runningId}", method = RequestMethod.DELETE)
    public void deleteByRunningId(@PathVariable("runningId") String runningId) {
        runningInformationService.deleteByRunningId(runningId);
    }

    @RequestMapping(value = "/runninginformations/all", method = RequestMethod.GET)
    public Page<RunningInformation> findAllInfo(@RequestParam(name = "page", defaultValue = kDefaultPage) Integer page,
                                                @RequestParam(name = "size", defaultValue = kDefaultItemPerPage) Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "healthWarningLevel");
        Pageable pageable = new PageRequest(page, size, sort);
        return runningInformationService.findAll(pageable);
    }

    @RequestMapping(value = "/runninginformations", method = RequestMethod.GET)
    public ResponseEntity<List<JSONObject>> findAll(@RequestParam(name = "page", defaultValue = kDefaultPage) Integer page,
                                                    @RequestParam(name = "size", defaultValue = kDefaultItemPerPage) Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "heartRate");
        Pageable pageable = new PageRequest(page, size, sort);
        Page<RunningInformation> originResults = runningInformationService.findAll(pageable);
        List<RunningInformation> content = originResults.getContent();
        List<JSONObject> results = new ArrayList<JSONObject>();
        for (RunningInformation item : content) {
            JSONObject info = new JSONObject();
            info.put(runningId, item.getRunningId());
            info.put(totalRunningTime, item.getTotalRunningTime());
            info.put(heartRate, item.getHeartRate());
            info.put(userId, item.getId());
            info.put(userName, item.getUsername());
            info.put(userAddress, item.getAddress());
            info.put(healthWarningLevel, item.getHealthWarningLevel());
            results.add(info);
        }
        return new ResponseEntity<List<JSONObject>>(results, HttpStatus.OK);
    }

    @RequestMapping(value = "/runninginformations", method = RequestMethod.DELETE)
    public void purge() {
        this.runningInformationService.deleteAll();
    }


    @RequestMapping(value = "/runninginformations/ran", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRandomOne() {
        runningInformationService.saveRandomOne(RunningInformation.autoGenerate());
    }

}
