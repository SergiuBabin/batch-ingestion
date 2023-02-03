package itss.batch.ingestion.job;

import itss.batch.ingestion.listener.JobExecutionListenerImpl;
import itss.batch.ingestion.model.csv.LegalEntityCsvTemplate;
import itss.batch.ingestion.model.request.LegalEntityRequestTemplate;
import itss.batch.ingestion.policy.FullIngestionSkipper;
import itss.batch.ingestion.processor.LegalEntityProcessorTemplate;
import itss.batch.ingestion.reader.LegalEntityFileReaderTemplate;
import itss.batch.ingestion.writer.LegalEntityWriterTemplate;
import lombok.RequiredArgsConstructor;
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
public class TransfersJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    // To be added reader, processor and writer for Transfers
    // this is temporar items
    private final LegalEntityFileReaderTemplate legalEntityFileReader;
    private final LegalEntityProcessorTemplate legalEntityProcessor;
    private final LegalEntityWriterTemplate legalEntityWriter;

    private final JobExecutionListenerImpl jobExecutionListener;
    private final FullIngestionSkipper fullIngestionSkipper;

    @Bean
    @Qualifier("transfersJob")
    public Job transfersJobBuilder() {
        return jobBuilderFactory.get("TransfersJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener)
                .start(transfersStep())
                .build();
    }



    private Step transfersStep() {
        return stepBuilderFactory.get("Transfers Job Step")
                .<LegalEntityCsvTemplate, LegalEntityRequestTemplate>chunk(1)
                .reader(legalEntityFileReader)
                .processor(legalEntityProcessor)
                .writer(legalEntityWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipPolicy(fullIngestionSkipper)
                .build();
    }
}
