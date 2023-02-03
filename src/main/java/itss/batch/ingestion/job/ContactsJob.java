package itss.batch.ingestion.job;

import itss.batch.ingestion.listener.JobExecutionListenerImpl;
import itss.batch.ingestion.model.csv.ContactsCsv;
import itss.batch.ingestion.model.request.ContactsRequest;
import itss.batch.ingestion.processor.ContactsProcessor;
import itss.batch.ingestion.policy.FullIngestionSkipper;
import itss.batch.ingestion.reader.ContactsFileReader;
import itss.batch.ingestion.writer.ContactsWriter;
import lombok.RequiredArgsConstructor;
import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ContactsJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ContactsFileReader contactsFileReader;
    private final ContactsProcessor contactsProcessor;
    private final ContactsWriter contactsWriter;
    private final JobExecutionListenerImpl jobExecutionListener;
    private final FullIngestionSkipper fullIngestionSkipper;

    @Bean
    @Qualifier("contactsJob")
    public Job contactsJobBuilder() {
        return jobBuilderFactory.get("ContactsJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener)
                .start(contactsStep())
                .build();
    }

    private Step contactsStep() {
        return stepBuilderFactory.get("Contacts Job Step")
                .<ContactsCsv, ContactsRequest>chunk(1)
                .reader(contactsFileReader)
                .processor(contactsProcessor)
                .writer(contactsWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipPolicy(fullIngestionSkipper)
                .retryLimit(1)
                .retry(ConnectTimeoutException.class)
                .build();
    }

}
