package com.self.batchsample.jobs.jpa.jpawriter;

import com.self.batchsample.jobs.models.JpaCustomer;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class JpaItemInsertDatabaseJobConfig {

    public static final int CHUNK_SIZE = 100;
    public static final String ENCODING = "UTF-8";
    public static final String JPA_ITEM_INSERT_DATABASE_WRITER_JOB = "JPA_ITEM_INSERT_DATABASE_WRITER_JOB";

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Bean
    public FlatFileItemReader<JpaCustomer> task06FlatFileItemReader() {
        return new FlatFileItemReaderBuilder<JpaCustomer>()
                .name("task06FlatFileItemReader")
                .resource(new ClassPathResource("./customer.csv"))
                .encoding(ENCODING)
                .delimited().delimiter(",")
                .names("name", "age", "gender")
                .targetType(JpaCustomer.class)
                .build();
    }

    /**
     * JpaCustomer 엔티티를 DB에 저장하기 위한 JpaItemWriter
     * @return JpaCustomer 엔티티의 영속화를 위한 JpaItemWriter 인스턴스를 반환
     * JpaItemWriter<JpaCustomer> 인스턴스를 반환하면 배치 프로세스가 flush를 통해 Customer 데이터를 DB에 저장함
     */
    @Bean
    public JpaItemWriter<JpaCustomer> jpaItemWriter() {
        return new JpaItemWriterBuilder<JpaCustomer>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true) // 데이터베이스에 엔티티가 이미 존재하지 않는다고 가정하고 항상 새로운 데이터를 추가
                .build();
    }

    @Bean
    public Step task06FlatFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("------------- Init flatFileStep -------------");
        return new StepBuilder("task06FlatFileStep", jobRepository)
                .<JpaCustomer, JpaCustomer>chunk(CHUNK_SIZE, transactionManager)
                .reader(task06FlatFileItemReader())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    public Job task06FlatFileJob(Step task06FlatFileStep, JobRepository jobRepository) {
        log.info("------------- Init flatFileJob -------------");

        return new JobBuilder(JPA_ITEM_INSERT_DATABASE_WRITER_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(task06FlatFileStep)
                .build();
    }
}
