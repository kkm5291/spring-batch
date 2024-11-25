//package com.self.batchsample.jobs.flatfilereader;
//
//import com.self.batchsample.jobs.models.Customer;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.FlatFileItemWriter;
//import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
//import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Slf4j
//@Configuration
//public class FlatFileItemJobConfig {
//
//
//    /**
//     * Chunk 사이즈 지정
//     */
//    public static final int CHUNK_SIZE = 100;
//    public static final String ENCODING = "UTF-8";
//    public static final String FLAT_FILE_CHUNK_JOB = "FLAT_FILE_CHUNK_JOB";
//
//    /**
//     * Spring Batch에서 제공하는 기본적인 ItemReader
//     * 텍스트 파일에서 데이터를 읽음.
//     * 간단하고 효율적인 구현 : 설정 및 사용이 간편하며, 대규모 데이터 처리에도 효율적임
//     * 다양한 텍스트 파일 형식 지원
//     * 확장 가능성
//     * 그러나 복잡한 데이터 구조 처리에는 적합하지 않음
//     * @return
//     */
//    @Bean
//    public FlatFileItemReader<Customer> flatFileItemReader() {
//
//        return new FlatFileItemReaderBuilder<Customer>()
//                .name("FlatFileItemReader")
//                .resource(new ClassPathResource("./customer.csv")) // 읽을 파일 지정
//                .encoding(ENCODING)
//                .delimited().delimiter(",")
//                .names("name", "age", "gender")
//                .targetType(Customer.class)
//                .build();
//    }
//
//    @Bean
//    public FlatFileItemWriter<Customer> flatFileItemWriter() {
//        return new FlatFileItemWriterBuilder<Customer>()
//                .name("flatFileItemWriter")
//                .resource(new FileSystemResource("./output/customer_new.csv"))
//                .encoding(ENCODING)
//                .delimited().delimiter("\t")
//                .names("Name", "Age", "Gender")
//                .build();
//    }
//
//
//    @Bean
//    public Step flatFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        log.info("------------------ Init flatFileStep -----------------");
//
//        return new StepBuilder("flatFileStep", jobRepository)
//                .<Customer, Customer>chunk(CHUNK_SIZE, transactionManager)
//                .reader(flatFileItemReader())
//                .writer(flatFileItemWriter())
//                .build();
//    }
//
//    @Bean
//    public Job flatFileJob(Step flatFileStep, JobRepository jobRepository) {
//        log.info("------------------ Init flatFileJob -----------------");
//        return new JobBuilder(FLAT_FILE_CHUNK_JOB, jobRepository)
//                .incrementer(new RunIdIncrementer())
//                .start(flatFileStep)
//                .build();
//    }
//
//}
