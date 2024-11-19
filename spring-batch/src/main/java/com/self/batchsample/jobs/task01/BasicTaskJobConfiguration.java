package com.self.batchsample.jobs.task01;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BasicTaskJobConfiguration {

    /**
     * 스프링 배치는 데이터 소스와 함께 작업하기 때문에 PlatformTransactionManager가 필요함.
     */
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Tasklet greetingTasklet() {
        return new GreetingTask();
    }

    /**
     * StepBuilder를 생성하고, 스텝의 이름을 myStep 으로 지정했다.
     * 그리고 이 스텝은 jobRepository에 등록한다.
     * tasklet을 스텝에 추가하고, greetingTasklet()을 통해 싱글톤 GreetingTask를 주입했다
     * @param jobRepository
     * @param transactionManager
     * @return
     */
    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("--------------------- Init myStep ---------------------");

        return new StepBuilder("myStep", jobRepository)
                .tasklet(greetingTasklet(), transactionManager)
                .build();
    }

    /**
     * Job은 step과 jobRepository가 필요하다.
     * incremeter는 job이 지속적으로 실행 될 때, job의 유니크성을 구분할 수 있는 방법을 설정한다.
     * start(step)을 통해서 job의 시작 포인트를 잡는다.
     * 처음 시작하는 step은 우리가 파라미터로 받은 step을 등록했다.
     * @param step
     * @param jobRepository
     * @return
     */
    @Bean
    public Job myJob(Step step, JobRepository jobRepository) {
        log.info("--------------------- Init myJob ---------------------");
        return new JobBuilder("myJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }
}
