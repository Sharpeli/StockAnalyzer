package com.spring.stockAnalyzer.infrastructure.mybatis;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiConsumer;

@Component
public class BatchExecutor {
    private final SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    public BatchExecutor(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public <TEntity, TMapper> void executeInBatch(List<TEntity> entities, Class<TMapper> mapperClass, BiConsumer<TEntity, TMapper> action) {
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        TMapper mapper = session.getMapper(mapperClass);
        try {
            int i = 0;
            for(TEntity entity: entities) {
                action.accept(entity, mapper);
                i++;
                if(i % 1000 == 0 || (i == entities.size())) {
                    session.commit();
                    session.clearCache();
                }
            }
        } catch (Exception e) {
            session.rollback();
            throw e;
        } finally {
            session.close();
        }
    }


}
