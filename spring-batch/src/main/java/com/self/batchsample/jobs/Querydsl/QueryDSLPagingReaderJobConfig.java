package com.self.batchsample.jobs.Querydsl;

import com.self.batchsample.jobs.models.JpaCustomer;
import com.self.batchsample.jobs.models.QJpaCustomer;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class QueryDSLPagingReaderJobConfig {

    public static final int CHUNK_SIZE = 2;
    public static final String ENCODING = "UTF-8";
    public static final String QUERYDSL_PAGING_CHUNK_JOB = "QUERYDSL_PAGING_CHUNK_JOB";

    @Autowired
    DataSource dataSource;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Bean
    public QuerydslPagingItemReader<JpaCustomer> task09CustomerQuerydslPagingItemReader() {
        return new QuerydslPagingItemReaderBuilder<JpaCustomer>()
                .name("task09CustomerQuerydslPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .chunkSize(CHUNK_SIZE)
                .querySupplier(jpaQueryFactory -> jpaQueryFactory
                        .select(QJpaCustomer.jpaCustomer)
                        .from(QJpaCustomer.jpaCustomer)
                        .where(QJpaCustomer.jpaCustomer.age.gt(20)))
                .build();
    }
}
