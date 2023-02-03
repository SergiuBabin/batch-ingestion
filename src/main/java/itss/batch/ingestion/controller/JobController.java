package itss.batch.ingestion.controller;

import itss.batch.ingestion.model.request.JobStatusRequest;
import itss.batch.ingestion.model.request.JobTriggerRequest;
import itss.batch.ingestion.model.response.JobResponse;
import itss.batch.ingestion.model.response.JobStatusResponse;
import itss.batch.ingestion.service.JobService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class JobController {

    private final JobService jobService;

    @PostMapping("/fullIngestionJob")
    @ApiOperation(value = "", notes = "", response = JobResponse.class)
    public ResponseEntity<JobResponse> startJob(@RequestBody @Valid JobTriggerRequest jobTriggerRequest) {
        return new ResponseEntity<>(jobService.startJob(jobTriggerRequest), HttpStatus.OK);
    }

    @PostMapping("/status")
    @ApiOperation(value = "", notes = "", response = JobStatusResponse.class)
    public ResponseEntity<JobStatusResponse> jobStatus(@RequestBody @Valid JobStatusRequest jobStatusRequest) {
        return new ResponseEntity<>(jobService.jobStatus(jobStatusRequest), HttpStatus.OK);
    }

}
