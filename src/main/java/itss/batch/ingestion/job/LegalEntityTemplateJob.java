package itss.batch.ingestion.job;

import itss.batch.ingestion.listener.JobExecutionListenerImpl;
import itss.batch.ingestion.model.csv.LegalEntityCsvTemplate;
import itss.batch.ingestion.model.request.LegalEntityRequestTemplate;
import itss.batch.ingestion.policy.FullIngestionSkipper;
import itss.batch.ingestion.processor.LegalEntityProcessorTemplate;
import itss.batch.ingestion.reader.LegalEntitySecondFileReaderTemplate;
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
public class LegalEntityTemplateJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final LegalEntitySecondFileReaderTemplate legalEntityFileReader;
    private final LegalEntityProcessorTemplate legalEntityProcessor;
    private final LegalEntityWriterTemplate legalEntityWriter;
    private final JobExecutionListenerImpl jobExecutionListner;
    private final FullIngestionSkipper fullIngestionSkipper;

    @Bean
    @Qualifier("legalEntityJob")
    public Job legalEntityJobBuilder() {
        return jobBuilderFactory.get("SavingsJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListner)
                .start(legalEntityStep())
                .build();
    }

    private Step legalEntityStep() {
        return stepBuilderFactory.get("Savings Step")
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
