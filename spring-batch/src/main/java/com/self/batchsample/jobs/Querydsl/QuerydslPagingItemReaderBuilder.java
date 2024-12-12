package com.self.batchsample.jobs.Querydsl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.util.ClassUtils;

import java.util.function.Function;

public class QuerydslPagingItemReaderBuilder<T> {

    private EntityManagerFactory entityManagerFactory;
    private Function<JPAQueryFactory, JPAQuery<T>> querySupplier;

    private int chunkSize = 10;

    private String name;

    private Boolean alwaysReadFromZero;

    public QuerydslPagingItemReaderBuilder<T> entityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        return this;
    }

    public QuerydslPagingItemReaderBuilder<T> querySupplier(Function<JPAQueryFactory, JPAQuery<T>> querySupplier) {
        this.querySupplier = querySupplier;
        return this;
    }

    public QuerydslPagingItemReaderBuilder<T> chunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
        return this;
    }

    public QuerydslPagingItemReaderBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public QuerydslPagingItemReaderBuilder<T> alwaysReadFromZero(Boolean alwaysReadFromZero) {
        this.alwaysReadFromZero = alwaysReadFromZero;
        return this;
    }

    public QuerydslPagingItemReader<T> build() {
        if (name == null) {
            this.name = ClassUtils.getShortName(QuerydslPagingItemReader.class);
        }
        if (this.entityManagerFactory == null) {
            throw new IllegalArgumentException("EntityManagerFactory is required");
        }
        if (this.querySupplier == null) {
            throw new IllegalArgumentException("querySupplier is required");
        }
        if (this.alwaysReadFromZero == null) {
            alwaysReadFromZero = false;
        }
        return new QuerydslPagingItemReader<>(name, entityManagerFactory, querySupplier, chunkSize, alwaysReadFromZero);
    }
}
