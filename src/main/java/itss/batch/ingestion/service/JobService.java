package itss.batch.ingestion.service;

import itss.batch.ingestion.exceptions.NoDataFoundException;
import itss.batch.ingestion.model.entity.BatchJobMapping;
import itss.batch.ingestion.model.request.JobStatusRequest;
import itss.batch.ingestion.model.request.JobTriggerRequest;
import itss.batch.ingestion.model.response.*;
import itss.batch.ingestion.repo.JobMappingRepository;
import itss.batch.ingestion.constants.JobParametersConstants;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobService {

    @Autowired
    @Qualifier("contactsJob")
    private final Job contactsJob;

    @Autowired
    @Qualifier("transfersJob")
    private final Job transfersJob;

    @Autowired
    @Qualifier("legalEntityJob")
    private final Job legalEntityJob;

    private final JobLauncher simpleJobLauncher;
    private final JobExplorerFactoryBean jobExplorerFactoryBean;
    private final JobMappingRepository jobMappingRepository;

    @SneakyThrows
    @Async
    public JobResponse startJob(JobTriggerRequest jobTriggerRequest) {
        Map<String, JobParameter> params = new HashMap<>();
        params.put(JobParametersConstants.CURRENT_TIME, new JobParameter(System.currentTimeMillis()));
        params.put(JobParametersConstants.SERVICE_AGREEMENT_ID, new JobParameter(jobTriggerRequest.getServiceAgreementId()));
        params.put(JobParametersConstants.EXTERNAL_USER_ID, new JobParameter(jobTriggerRequest.getExternalUserId()));

        JobParameters jobParameters = new JobParameters(params);
        JobResponse jobResponse = new JobResponse();
        PartialIngestion partialIngestion = new PartialIngestion();
        List<BatchJobMapping> batchJobMapping = new ArrayList<>();
        String fullIngestionJobId = UUID.randomUUID().toString();
        JobExecution jobExecution;

        if (Boolean.FALSE.equals(jobTriggerRequest.getSkipJobs().getSkipSavings())) {
            jobExecution = simpleJobLauncher.run(legalEntityJob, jobParameters);
            partialIngestion.setSavingsJobId(jobExecution.getJobId().toString());
            batchJobMapping.add(BatchJobMapping.builder().instanceId(jobExecution.getJobId())
                    .jobName(jobExecution.getJobInstance().getJobName())
                    .jobMasterId(fullIngestionJobId)
                    .build());
        }

        if (Boolean.FALSE.equals(jobTriggerRequest.getSkipJobs().getSkipContacts())) {
            jobExecution = simpleJobLauncher.run(contactsJob, jobParameters);
            partialIngestion.setContactsJobId(jobExecution.getJobId().toString());
            batchJobMapping.add(BatchJobMapping.builder().instanceId(jobExecution.getJobId())
                    .jobName(jobExecution.getJobInstance().getJobName())
                    .jobMasterId(fullIngestionJobId)
                    .build());
        }

        if (Boolean.FALSE.equals(jobTriggerRequest.getSkipJobs().getSkipTransfers())) {
            jobExecution = simpleJobLauncher.run(transfersJob, jobParameters);
            partialIngestion.setTransfersJobId(jobExecution.getJobId().toString());
            batchJobMapping.add(BatchJobMapping.builder().instanceId(jobExecution.getJobId())
                    .jobName(jobExecution.getJobInstance().getJobName())
                    .jobMasterId(fullIngestionJobId)
                    .build());
        }

        jobMappingRepository.saveAll(batchJobMapping);

        jobResponse.setFullIngestionJobId(fullIngestionJobId);
        jobResponse.setPartialIngestion(partialIngestion);
        log.debug("JobExecution Id's = {}", jobResponse);

        return jobResponse;
    }

    @SneakyThrows
    @Async
    public JobStatusResponse jobStatus(JobStatusRequest jobStatusRequest) {
        Map<JobStatus, Integer> jobStatuses = new EnumMap<>(JobStatus.class);

        JobExplorer jobExplorer = Optional.ofNullable(jobExplorerFactoryBean.getObject())
                .orElseThrow(NoDataFoundException::new);

        PartialIngestionStatus partialIngestionStatus = new PartialIngestionStatus();
        JobExecution jobExecution;
        List<Long> jobIds = new ArrayList<>();
        BatchJobMapping batchJob = null;
        if (jobStatusRequest.getFullIngestionJobId() == null) {
            if (jobStatusRequest.getContactsJobId() != null) {
                jobExecution = Optional.ofNullable(jobExplorer.getJobExecution(Long.valueOf(jobStatusRequest.getContactsJobId())))
                        .orElseThrow(NoDataFoundException::new);
                partialIngestionStatus.setContactsJobStatus(convertToJobStatus(jobExecution.getStatus()));
                jobIds.add(jobExecution.getJobId());
            }

            if (jobStatusRequest.getTransfersJobId() != null) {
                jobExecution = Optional.ofNullable(jobExplorer.getJobExecution(Long.valueOf(jobStatusRequest.getTransfersJobId())))
                        .orElseThrow(NoDataFoundException::new);
                partialIngestionStatus.setTransfersJobStatus(convertToJobStatus(jobExecution.getStatus()));
                jobIds.add(jobExecution.getJobId());
            }

            if (jobStatusRequest.getSavingsJobId() != null) {
                jobExecution = Optional.ofNullable(jobExplorer.getJobExecution(Long.valueOf(jobStatusRequest.getSavingsJobId())))
                        .orElseThrow(NoDataFoundException::new);
                partialIngestionStatus.setSavingsJobStatus(convertToJobStatus(jobExecution.getStatus()));
                jobIds.add(jobExecution.getJobId());
            }
            batchJob = jobMappingRepository.getById(jobIds.get(0));
            jobStatusRequest.setFullIngestionJobId(batchJob.getJobMasterId());
        }

        List<BatchJobMapping> batchJobMappings =
                Optional.ofNullable(jobMappingRepository.findByJobMasterId(jobStatusRequest.getFullIngestionJobId()))
                        .orElseThrow(NoDataFoundException::new);

        if (batchJobMappings.isEmpty()) {
            throw new NoDataFoundException();
        }

        PartialIngestionStatus partialIngestionStatusFull = new PartialIngestionStatus();

        for (BatchJobMapping batchJobMapping : batchJobMappings) {
            jobExecution = Optional.ofNullable(jobExplorer.getJobExecution(batchJobMapping.getInstanceId()))
                    .orElseThrow(NoDataFoundException::new);

            JobStatus jobStatus = convertToJobStatus(jobExecution.getStatus());
            jobStatuses.put(jobStatus, jobStatuses.get(jobStatus) == null ? 0 : jobStatuses.get(jobStatus) + 1);

            switch (batchJobMapping.getJobName()) {
                case "ContactsJob":
                    partialIngestionStatusFull.setContactsJobStatus(jobStatus);
                    break;
                case "TransfersJob":
                    partialIngestionStatusFull.setTransfersJobStatus(jobStatus);
                    break;
                case "SavingsJob":
                    partialIngestionStatusFull.setSavingsJobStatus(jobStatus);
                    break;
                default:
                    break;
            }
        }

        return JobStatusResponse.builder()
                .fullIngestionJobStatus(fullIngestionJobStatus(jobStatuses))
                .partialIngestionStatus(batchJob != null ? partialIngestionStatus : partialIngestionStatusFull)
                .build();
    }

    private JobStatus convertToJobStatus(BatchStatus batchStatus) {
        if (batchStatus.equals(BatchStatus.COMPLETED)) {
            return JobStatus.DONE;
        }

        if (batchStatus.equals(BatchStatus.STARTED) || batchStatus.equals(BatchStatus.STARTING)) {
            return JobStatus.IN_PROCESSING;
        }

        return JobStatus.FAILED;
    }

    private JobStatus fullIngestionJobStatus(Map<JobStatus, Integer> jobStatuses) {
        if (jobStatuses.get(JobStatus.FAILED) != null && jobStatuses.get(JobStatus.DONE) != null && jobStatuses.get(JobStatus.IN_PROCESSING) == null) {
            return JobStatus.PARTIAL_DONE;
        } else if (jobStatuses.get(JobStatus.IN_PROCESSING) != null) {
            return JobStatus.IN_PROCESSING;
        } else if (jobStatuses.get(JobStatus.FAILED) != null) {
            return JobStatus.FAILED;
        }

        return JobStatus.DONE;
    }

}
