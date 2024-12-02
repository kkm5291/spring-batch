package com.self.batchsample.jobs.jpa;

import com.self.batchsample.jobs.models.JpaCustomer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class CustomerItemProcessor implements ItemProcessor<JpaCustomer, JpaCustomer> {
    @Override
    public JpaCustomer process(JpaCustomer item) throws Exception {
        log.info("Item Processor ------------- {}", item);
        return item;
    }
}
