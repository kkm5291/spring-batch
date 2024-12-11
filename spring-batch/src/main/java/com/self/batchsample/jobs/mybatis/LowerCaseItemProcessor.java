package com.self.batchsample.jobs.mybatis;

import com.self.batchsample.jobs.models.MybatisCustomer;
import org.springframework.batch.item.ItemProcessor;

/**
 * 이름, 성별을 소문자로 변경하는 Processor
 */
public class LowerCaseItemProcessor implements ItemProcessor<MybatisCustomer, MybatisCustomer> {

    @Override
    public MybatisCustomer process(MybatisCustomer item) throws Exception {
        item.setName(item.getName().toLowerCase());
        item.setGender(item.getGender().toLowerCase());
        return item;
    }
}
