package itss.batch.ingestion.policy;

import itss.batch.ingestion.exceptions.ContactsBulkException;
import itss.batch.ingestion.utils.ReportUtils;
import itss.batch.ingestion.constants.JobParametersConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Slf4j
@Component
@StepScope
public class FullIngestionSkipper implements SkipPolicy {

    @Value("${report.path.errors}")
    private String errorsReportPath;

    @Value("#{stepExecution.jobExecution}")
    private JobExecution jobExecution;

    @Override
    public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
        String fullReportPath = ReportUtils.generateJobReportFullPath(errorsReportPath, jobExecution);
        String fileName = ReportUtils.generateReportName(jobExecution.getJobParameters().getString(JobParametersConstants.EXTERNAL_USER_ID), jobExecution.getStartTime(), true);
        String errorMessage;
        if (exception instanceof FileNotFoundException) {
            log.debug("File was not found!!!");
            return false;
        } else if (exception instanceof FlatFileParseException) {
            FlatFileParseException e = (FlatFileParseException) exception;
            errorMessage = ReportUtils.generateExceptionContent(e.getInput(), exception);
        } else if (exception instanceof ContactsBulkException) {
            ContactsBulkException e = (ContactsBulkException) exception;
            errorMessage = ReportUtils.generateExceptionContent(e.getContactsRequest(), exception);
        } else {
            errorMessage = ReportUtils.generateExceptionContent(null, exception);
        }
        ReportUtils.generateExceptionReport(fullReportPath, fileName, errorMessage);
        return true;
    }

}
