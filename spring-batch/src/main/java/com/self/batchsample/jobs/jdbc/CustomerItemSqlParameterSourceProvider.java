package com.self.batchsample.jobs.jdbc;

import com.self.batchsample.jobs.models.Customer;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class CustomerItemSqlParameterSourceProvider implements ItemSqlParameterSourceProvider<Customer> {
    @Override
    public SqlParameterSource createSqlParameterSource(Customer item) {

        // 클래스를 파라미터로 받아서 클래스에 있는 필드의 값을 자바 Bean Property 접근법을 통해 바꾼다.
        return new BeanPropertySqlParameterSource(item);
    }
}
