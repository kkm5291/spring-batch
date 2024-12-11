package com.self.batchsample.jobs.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.self.batchsample.jobs.models.MybatisCustomer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class MyBatisReaderJobConfig {

    public static final int CHUNK_SIZE = 2;
    public static final String ENCODING = "UTF-8";
    public static final String MYBATIS_CHUNK_JOB = "MYBATIS_CHUNK_JOB";

    @Autowired
    DataSource dataSource;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Bean
    public MyBatisPagingItemReader<MybatisCustomer> myBatisItemReader() throws Exception {

        return new MyBatisPagingItemReaderBuilder<MybatisCustomer>()
                .sqlSessionFactory(sqlSessionFactory) // 세션 팩토리 지정
                .pageSize(CHUNK_SIZE)
                .queryId("batchsample.jobs.selectCustomers") // 네임 스페이스 + SQL ID
                .build();
    }

    @Bean
    public FlatFileItemReader<MybatisCustomer> flatFileItemReader() throws Exception {

        return new FlatFileItemReaderBuilder<MybatisCustomer>()
                .name("FlatFileItemReader")
                .resource(new FileSystemResource("./output/task07_customer_new_v4.csv"))
                .encoding(ENCODING)
                .delimited().delimiter("\t")
                .names("id", "name", "age", "gender")
                .targetType(MybatisCustomer.class)
                .build();

    }

    // csv 파일로 writer 하는 역할
    @Bean
    public FlatFileItemWriter<MybatisCustomer> task07customerCursorFlatFileItemWriter() {
        return new FlatFileItemWriterBuilder<MybatisCustomer>()
                .name("customerCursorFlatFileItemWriter")
                .resource(new FileSystemResource("./output/task07_customer_new_v4.csv"))
                .encoding(ENCODING)
                .delimited().delimiter("\t")
                .names("id", "name", "age", "gender")
                .build();
    }

    // mybatis insert writer
    @Bean
    public MyBatisBatchItemWriter<MybatisCustomer> mybatisItemWriter() {
        return new MyBatisBatchItemWriterBuilder<MybatisCustomer>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("batchsample.jobs.insertCustomers")
                .build();
    }

    @Bean
    public Step task07customerJdbcCursorStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, MyBatisPagingItemReader<MybatisCustomer> myBatisItemReader) throws Exception {
        log.info("------------------- Init task07customerJdbcCursorStep -------------------");

        return new StepBuilder("customerJdbcCursorStep", jobRepository)
                .<MybatisCustomer, MybatisCustomer>chunk(CHUNK_SIZE, transactionManager)
                .reader(flatFileItemReader())
                .processor(new CustomerItemProcessor())
                .writer(mybatisItemWriter())
                .build();
    }

    @Bean
    public Job task07customerJdbcCursorPagingJob(Step task07customerJdbcCursorStep, JobRepository jobRepository) {
        log.info("------------------- Init task07customerJdbcCursorPagingJob -------------------");

        return new JobBuilder(MYBATIS_CHUNK_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(task07customerJdbcCursorStep)
                .build();
    }
}
