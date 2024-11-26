package com.self.batchsample.jobs.flatfilereader;

import com.self.batchsample.jobs.models.Customer;
import org.springframework.batch.item.file.transform.LineAggregator;

/**
 * FlatFile에 저장할 아이템들을 스트링으로 변환하는 방법을 지정하는 것.
 * 아래와 같이 aggregate를 구현하여 아이템을 스트링 문자로 변경할 수 있음.
 */
public class CustomerLineAggregator implements LineAggregator<Customer> {

    public static final String DELIMITER_FOR_WRITER = "\t";

    @Override
    public String aggregate(Customer item) {
        return item.getName() + DELIMITER_FOR_WRITER + item.getAge();
    }
}
