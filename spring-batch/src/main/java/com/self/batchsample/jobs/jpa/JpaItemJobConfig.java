package com.self.batchsample.jobs.jpa;

import com.self.batchsample.jobs.models.JpaCustomer;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public JpaPagingItemReader<JpaCustomer> customerJpaPagingItemReader() {

        return new JpaPagingItemReaderBuilder<JpaCustomer>()
                .name("customerJpaPagingItemReader")
                .queryString("SELECT jc FROM JpaCustomer jc WHERE jc.age > :age order by id desc")
                .pageSize(CHUNK_SIZE)
                .entityManagerFactory(entityManagerFactory)
                .parameterValues(Collections.singletonMap("age", 5))
                .build();
    }


}
