package com.self.batchsample.jobs.Querydsl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class QuerydslPagingItemReader<T> extends AbstractPagingItemReader<T> {

    private EntityManager em;
    private final Function<JPAQueryFactory, JPAQuery<T>> querySupplier;

    private final Boolean alwaysReadFromZero;

    public QuerydslPagingItemReader(EntityManagerFactory entityManagerFactory, Function<JPAQueryFactory, JPAQuery<T>> querySupplier, int chunkSize) {
        this(ClassUtils.getShortName(QuerydslPagingItemReader.class), entityManagerFactory, querySupplier, chunkSize, false);
    }

    /**
     *
     * @param name : ItemReader를 구분하기 위한 이름
     * @param entityManagerFactory : JPA를 이용하기 위해서 entityManagerFactory를 전달한다.
     * @param querySupplier : 이는 JPAQuery를 생성하기 위한 Functional Interface 이다.
     *                      입력 파라미터로 JPAQueryFactory를 입력으로 전달 받는다.
     *                      반환값은 JPAQuery 형태의 queryDSL 쿼리가 된다.
     * @param chunkSize : 한 번에 페이징 처리할 페이지 크기
     * @param alwaysReadFromZero : 항상 0부터 페이징을 읽을 지 여부를 정한다.
     *                           만약 paging 처리 된 데이터 자체를 수정하는 경우
     *                           배치처리 누락이 발생할 수 있으므로
     *                           이를 해결하기 위한 방안으로 사용함.
     */
    public QuerydslPagingItemReader(String name, EntityManagerFactory entityManagerFactory, Function<JPAQueryFactory, JPAQuery<T>> querySupplier, int chunkSize, Boolean alwaysReadFromZero) {
        super.setPageSize(chunkSize);
        setName(name);
        this.querySupplier = querySupplier;
        this.em = entityManagerFactory.createEntityManager();
        this.alwaysReadFromZero = alwaysReadFromZero;
    }

    @Override
    protected void doClose() throws Exception {
        if (em != null) {
            em.close();
        }
        super.doClose();
    }

    /**
     * JPAQueryFactory를 통해서 함수형 인터페이스로 지정된 queryDSL에 적용할 QueryFactory이다.
     *
     */
    @Override
    protected void doReadPage() {
        initQueryResult();

        /**
         * 이 부분은 스프링 부트 3.0 이상에서 부터 javax가 지원되지 않으므로
         * build.gradle 부분에서 querydsl:jakarta를 지정해줘야 함.
         */
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        long offset = 0;
        if (!alwaysReadFromZero) {
            offset = (long) getPage() * getPageSize();
        }

        JPAQuery<T> query = querySupplier.apply(jpaQueryFactory)
                .offset(offset)
                .limit(getPageSize());

        List<T> queryResult = query.fetch();
        for (T entity : queryResult) {
            em.detach(entity);
            results.add(entity);
        }

    }

    private void initQueryResult() {
        if (CollectionUtils.isEmpty(results)) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }
    }
}
