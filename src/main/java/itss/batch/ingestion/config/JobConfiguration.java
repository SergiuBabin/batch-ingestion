package itss.batch.ingestion.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JobConfiguration {
    private final JobRepository jobRepository;
    private final JobLauncher simpleJobLauncher;
    private final DataSource dataSource;
    private JobRepositoryFactoryBean jobRepositoryFactoryBean;
    private MapJobRegistry mapJobRegistry;
    private JobExplorerFactoryBean jobExplorer;
    private PlatformTransactionManager transactionManager;

    @Value("ISOLATION_DEFAULT")
    private String isolationLevelForCreate;

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    @Bean
    public JobLauncher simpleJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(taskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public SimpleJobOperator jobOperator(JobExplorer jobExplorer,
                                         JobRepository jobRepository,
                                         JobRegistry jobRegistry) {

        SimpleJobOperator jobOperator = new SimpleJobOperator();

        jobOperator.setJobExplorer(jobExplorer);
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setJobRegistry(jobRegistry);
        jobOperator.setJobLauncher(simpleJobLauncher);

        return jobOperator;
    }


    /**
     * This method is creating jobrepository
     *
     * @return JobRepositoryFactoryBean
     */
    @Bean(name = "jobRepositoryFactoryBean")
    public JobRepositoryFactoryBean jobRepositoryFactoryBean() {

        this.jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        this.jobRepositoryFactoryBean.setDataSource(dataSource);
        this.jobRepositoryFactoryBean.setTransactionManager(this.transactionManager);
        this.jobRepositoryFactoryBean.setIsolationLevelForCreate(this.isolationLevelForCreate);
        return this.jobRepositoryFactoryBean;
    }

    /**
     * This method is creating jobExplorer bean
     *
     * @return JobExplorerFactoryBean
     */
    @Bean
    @DependsOn("dataSource")
    public JobExplorerFactoryBean jobExplorerFactoryBean() {

        this.jobExplorer = new JobExplorerFactoryBean();
        this.jobExplorer.setDataSource(dataSource);
        return this.jobExplorer;
    }

    /**
     * This method is creating jobRegistry bean
     *
     * @return MapJobRegistry
     */
    @Bean
    public MapJobRegistry mapJobRegistry() {

        this.mapJobRegistry = new MapJobRegistry();
        return this.mapJobRegistry;
    }

    /**
     * This method is creating JobRegistryBeanPostProcessor
     *
     * @return JobRegistryBeanPostProcessor
     */
    @Bean
    @DependsOn("mapJobRegistry")
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {

        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(this.mapJobRegistry);
        return postProcessor;
    }

    /**
     * This method is creating incrementer
     *
     * @return RunIdIncrementer
     */
    @Bean
    public RunIdIncrementer incrementer() {

        return new RunIdIncrementer();
    }

    /**
     * @param transactionManager the transactionManager to set
     */
    @Inject
    public void setTransactionManager(PlatformTransactionManager transactionManager) {

        this.transactionManager = transactionManager;
    }
}

