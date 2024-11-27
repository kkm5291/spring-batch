//package com.self.batchsample.jobs.task01;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.StepContribution;
//import org.springframework.batch.core.scope.context.ChunkContext;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.InitializingBean;
//
//@Slf4j
//public class GreetingTask implements Tasklet, InitializingBean {
//
//    /**
//     * Tasklet 구현 메서드
//     * StepContribution과 ChunkContext를 파라미터로 받는다.
//     * 최종적으로 RepeatStatus를 반환하며 이 값은 다음과 같음.
//     *      FINISHED : 태스크릿이 종료되었음을 나타낸다.
//     *      CONTINUABLE : 계속해서 태스크를 수행하도록 한다.
//     *      continuelf(condition) : 조건에 따라 종료할 지 지속할 지 결정하는 메소드에 따라 종료/ 지속을 결정한다.
//     * afterPropertiesSet : 태스크를 수해할 때 프로퍼티를 설정하고 난 뒤에 수행되는 메서드. 없어도 됨
//     * @param contribution
//     * @param chunkContext
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//        log.info("--------------------- Task Execute ---------------------");
//        log.info("Greeting Task: {}, {}", contribution, chunkContext);
//
//        return RepeatStatus.FINISHED;
//    }
//
//    /**
//     * InitializeBean 구현 메서드
//     * @throws Exception
//     */
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        log.info("--------------------- After Properties Set() ---------------------");
//    }
//}
