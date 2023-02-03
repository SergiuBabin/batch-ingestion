package itss.batch.ingestion.listener;

import itss.batch.ingestion.utils.ReportUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static itss.batch.ingestion.constants.JobParametersConstants.EXTERNAL_USER_ID;

@Slf4j
@Component
public class JobExecutionListenerImpl implements JobExecutionListener {

    @Value("${report.path.general}")
    private String generalReportPath;

    @Value("${report.path.job}")
    private String jobReportPath;

    @Value("${lines.skip.contactsReader}")
    private Integer contactsLinesSkipped;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        int totalLines = 0, totalSuccessfulLines = 0, totalSkippedLines = 0;

        String fullReportPath = ReportUtils.generateJobReportFullPath(jobReportPath, jobExecution);

        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {

            int lines = stepExecution.getExecutionContext().getInt("FlatFileItemReader.read.count", 0) - contactsLinesSkipped;
            int readCount = stepExecution.getReadCount();
            int writeCount = stepExecution.getWriteCount();
            int processSkipCount = stepExecution.getProcessSkipCount();
            int skippedLines = stepExecution.getSkipCount();
            Date stepStartTime = stepExecution.getStartTime();
            totalLines += lines;
            totalSuccessfulLines += writeCount;
            totalSkippedLines += skippedLines;

            String jobReport = "#####################   GENERAL INFORMATION   #####################\n" +
                    "\nTotal number of lines in file - " + lines +
                    "\nImport process start time - " + stepStartTime +
                    "\nImport process end time - " + stepExecution.getEndTime() +
                    "\n\n\n#####################   SUCCESS REPORT   #####################\n" +
                    "\nTotal lines have been successfully read - " + readCount +
                    "\nTotal lines have been successfully processed - " + (readCount - processSkipCount) +
                    "\nTotal lines have been successfully written - " + writeCount +
                    "\n\n\n#####################   FAULT REPORT   #####################\n" +
                    "\nTotal lines could not be read successfully - " + stepExecution.getReadSkipCount() +
                    "\nTotal lines could not be processed successfully - " + processSkipCount +
                    "\nTotal lines could not be written successfully - " + stepExecution.getWriteSkipCount() +
                    "\nTotal lines were skipped - " + skippedLines;

            log.debug("Current step is: " + stepExecution.getStepName() + ". Step report has following content:\n" + jobReport);

            String fileName = ReportUtils.generateReportName(jobExecution.getJobParameters().getString(EXTERNAL_USER_ID), stepStartTime, false);

            ReportUtils.generateReport(fullReportPath, fileName, jobReport);

        }

        Date jobStartTime = jobExecution.getStartTime();

        String generalReport = "#####################   GENERAL INFORMATION   #####################\n" +
                "\nTotal number of lines in file - " + totalLines +
                "\nImport process start time - " + jobStartTime +
                "\nImport process end time - " + jobExecution.getEndTime() +
                "\n\n\n#####################   SUCCESS REPORT   #####################\n" +
                "\nTotal lines have been successfully executed - " + totalSuccessfulLines +
                "\n\n\n#####################   FAULT REPORT   #####################\n" +
                "\nTotal lines were skipped - " + totalSkippedLines;

        log.debug("General report has following content:\n" + generalReport);

        ReportUtils.generateGeneralReport(generalReportPath, generalReport, jobStartTime);
    }
}
