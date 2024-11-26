package com.self.batchsample.practice.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Player implements Serializable {

    private long no;
    private String name;
    private int age;
}
