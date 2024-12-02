package com.self.batchsample.jobs.jpa;

import com.self.batchsample.jobs.models.JpaCustomer;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Collections;

@Slf4j
@Configuration
public class JpaItemJobConfig {

    public static final int CHUNK_SIZE = 100;
    public static final String ENCODING = "UTF-8";
    public static final String JPA_ITEM_WRITER_JOB = "JPA_ITEM_WRITER_JOB";

    @Autowired
    EntityManagerFactory entityManagerFactory;

    /**
     * JpaPagingItemReader 생성자를 이용한 방법
     */
//    @Bean
//    public JpaPagingItemReader<JpaCustomer> customerJpaPagingItemReader() {
//        JpaPagingItemReader<JpaCustomer> jpaPagingItemReader = new JpaPagingItemReader<>();
//        jpaPagingItemReader.setQueryString(
//                "SELECT jc FROM JpaCustomer jc WHERE jc.age > :age order by id desc"
//        );
//        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
//        jpaPagingItemReader.setPageSize(CHUNK_SIZE);
//        jpaPagingItemReader.setParameterValues(Collections.singletonMap("age", 5));
//        return jpaPagingItemReader;
//    }

    /**
     * Builder를 사용한 방법
     */
    @Bean
    public JpaPagingItemReader<JpaCustomer> task06CustomerJpaPagingItemReader() {

        return new JpaPagingItemReaderBuilder<JpaCustomer>()
                .name("task06CustomerJpaPagingItemReader")
                .queryString("SELECT jc FROM JpaCustomer jc WHERE jc.age > :age order by id desc")
                .pageSize(CHUNK_SIZE)
                .entityManagerFactory(entityManagerFactory)
                .parameterValues(Collections.singletonMap("age", 5))
                .build();
    }

    /**
     * JPA로 값 읽어서 flat File 형태로 파일 내보내기
     * @return
     */
    @Bean
    public FlatFileItemWriter<JpaCustomer> task06CustomerJpaFlatFileItemWriter() {
        
        return new FlatFileItemWriterBuilder<JpaCustomer>()
                .name("task06CustomerJpaFlatFileItemWriter")
                .resource(new FileSystemResource("./output/customer_new_v2.csv"))
                .encoding(ENCODING)
                .delimited().delimiter("\t")
                .names("name", "age", "gender")
                .build();
    }

    @Bean
    public Step customerJpaPagingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        log.info("------------------- Init customerJpaPagingStep -------------------");

        return new StepBuilder("customerJpaPagingStep", jobRepository)
                .<JpaCustomer, JpaCustomer>chunk(CHUNK_SIZE, transactionManager)
                .reader(task06CustomerJpaPagingItemReader())
                .processor(new CustomerItemProcessor())
                .writer(task06CustomerJpaFlatFileItemWriter())
                .build();
    }

    @Bean
    public Job customerJpaPagingJob(JobRepository jobRepository, Step customerJpaPagingStep) {
        return new JobBuilder(JPA_ITEM_WRITER_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(customerJpaPagingStep)
                .build();
    }
}
