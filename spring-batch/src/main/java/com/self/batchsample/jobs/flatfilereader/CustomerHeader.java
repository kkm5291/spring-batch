package com.self.batchsample.jobs.flatfilereader;

import com.self.batchsample.jobs.models.Customer;
import org.springframework.batch.item.file.FlatFileHeaderCallback;

import java.io.IOException;
import java.io.Writer;

/**
 * 출력될 파일의 헤더를 달아주는 역할
 */
public class CustomerHeader implements FlatFileHeaderCallback {
    public static final String DELIMITER_FOR_WRITER = "\t";

    @Override
    public void writeHeader(Writer writer) throws IOException {
        writer.write("ID" + DELIMITER_FOR_WRITER + "AGE");
    }
}
