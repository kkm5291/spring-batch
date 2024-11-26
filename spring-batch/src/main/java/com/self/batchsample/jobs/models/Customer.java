package com.self.batchsample.jobs.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // 배치 변환 과정에서 Setter가 사용될 수 있음.
public class Customer {

    private String name;
    private int age;
    private String gender;

}
