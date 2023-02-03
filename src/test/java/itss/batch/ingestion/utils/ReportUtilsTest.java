package itss.batch.ingestion.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;

import java.util.Calendar;

class ReportUtilsTest {

    @Test
    void testGenerateReportNameWithUserId() {
        String externalUserId = "300000005";
        String expectedExceptionReportName = "error_300000005_20220421160000";
        String expectedChildReportName = "result_300000005_20220421160000";

        testGenerateReportName(externalUserId, expectedExceptionReportName, expectedChildReportName);
    }

    @Test
    void testGenerateReportNameWithoutUserId() {
        String expectedExceptionReportName = "error_batch_20220421160000";
        String expectedChildReportName = "result_batch_20220421160000";

        testGenerateReportName(null, expectedExceptionReportName, expectedChildReportName);
    }

    private void testGenerateReportName(String externalUserId, String expectedExceptionReportName, String expectedChildReportName) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.APRIL, 21, 16, 0, 0);

        String actualExceptionReportName = ReportUtils.generateReportName(externalUserId, calendar.getTime(), true);
        String actualChildReportName = ReportUtils.generateReportName(externalUserId, calendar.getTime(), false);

        Assertions.assertEquals(expectedExceptionReportName, actualExceptionReportName);
        Assertions.assertEquals(expectedChildReportName, actualChildReportName);
    }

    @Test
    void testGetFormattedJobName() {
        JobExecution jobExecution = new JobExecution(new JobInstance(1L, "Test Job"), new JobParameters());

        String expectedFormattedJobName = "testjob";
        String actualFormattedJobName = ReportUtils.getFormattedJobName(jobExecution);

        Assertions.assertEquals(expectedFormattedJobName, actualFormattedJobName);
    }
}
