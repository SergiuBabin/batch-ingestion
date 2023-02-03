package itss.batch.ingestion.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ReportUtils {

    private static final String DATE_FORMAT = "yyyyMMddHHmmss";

    public static void generateReport(String reportPath, String fileName, String data) {
        File file = new File(reportPath, fileName + ".txt");

        try {
            if (!file.exists()) {
                boolean isSuccessfulFolder = file.getParentFile().mkdirs();
                boolean isSuccessfulFile = file.createNewFile();
                if (isSuccessfulFolder && isSuccessfulFile) {
                    log.debug("File {} was successfully created!", fileName + ".txt");
                }
            }
            try (FileWriter fw = new FileWriter(file.getAbsoluteFile())) {
                try (BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write(data);
                }
            }
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }

    public static void generateGeneralReport(String reportPath, String data, Date dateTime) {
        generateReport(reportPath, "result_" + getFormattedDateTime(DATE_FORMAT, dateTime), data);
    }

    public static void generateExceptionReport(String fullPath, String fileName, String errorMessage) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fullPath, fileName + ".txt"))) {
            errorMessage = reader.lines().collect(Collectors.joining(System.lineSeparator())) + errorMessage;
        } catch (IOException e) {
            log.error("Unfortunately, there are some exceptions on read, process or write! Please check error report!");
        }
        generateReport(fullPath, fileName, errorMessage);
    }

    public static String getFormattedDateTime(String pattern, Date dateTime) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        if (Objects.nonNull(dateTime)) {
            calendar.setTime(dateTime);
        }
        return dateFormat.format(calendar.getTime());
    }

    public static String generateJobReportFullPath(String reportPath, JobExecution jobExecution) {
        return MessageFormat.format(reportPath, ReportUtils.getFormattedJobName(jobExecution));
    }

    public static String getFormattedJobName(JobExecution jobExecution) {
        return jobExecution.getJobInstance().getJobName().replace(" ", "").toLowerCase(Locale.ROOT);
    }

    public static String generateReportName(String externalUserId, Date dateTime, boolean isExceptionReport) {
        if (Objects.isNull(externalUserId)) {
            externalUserId = "batch";
        }
        if (isExceptionReport) {
            return "error_" + externalUserId + "_" + ReportUtils.getFormattedDateTime(DATE_FORMAT, dateTime);
        }
        return "result_" + externalUserId + "_" + ReportUtils.getFormattedDateTime(DATE_FORMAT, dateTime);
    }

    public static <T> String generateExceptionContent(T object, Throwable exception) {
        StringBuilder stringBuilder = new StringBuilder();
        if (Objects.nonNull(object)) {
            stringBuilder.append("#####################   FAULT ROW   #####################\n")
                    .append(object).append("\n")
                    .append("#####################   FAULT MESSAGE   #####################\n")
                    .append(exception.getMessage()).append("\n\n");
        } else {
            stringBuilder.append("#####################   STACK TRACE   #####################\n")
                    .append(Arrays.toString(exception.getStackTrace())).append("\n\n");
        }
        String exceptionContent = stringBuilder.toString();
        log.debug("Was detected following exception:\n" + exceptionContent.trim());
        return exceptionContent;
    }
}
