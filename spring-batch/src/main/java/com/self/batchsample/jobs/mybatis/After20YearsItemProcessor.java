package com.self.batchsample.jobs.mybatis;

import com.self.batchsample.jobs.models.MybatisCustomer;
import org.springframework.batch.item.ItemProcessor;

/**
 * 나이에 20 추가하는 프로세서
 */
public class After20YearsItemProcessor implements ItemProcessor<MybatisCustomer, MybatisCustomer> {
    @Override
    public MybatisCustomer process(MybatisCustomer item) throws Exception {
        item.setAge(item.getAge() + 20);
        return item;
    }
}
