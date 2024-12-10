package com.self.batchsample;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchApplication {

    /**
     * 기본적으로 afterPropertySet(), Job, Step, Tasklet 순으로 실행?
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

}
