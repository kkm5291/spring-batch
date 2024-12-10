package com.self.batchsample.jobs.mybatis;

import com.self.batchsample.jobs.models.MybatisCustomer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class CustomerItemProcessor implements ItemProcessor<MybatisCustomer, MybatisCustomer> {
    @Override
    public MybatisCustomer process(MybatisCustomer item) throws Exception {
        log.info("Item Processor ------------- {}", item);
        return item;
    }
}
