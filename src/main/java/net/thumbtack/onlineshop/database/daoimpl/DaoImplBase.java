package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.mybatis.mappers.CommonMapper;
import net.thumbtack.onlineshop.database.mybatis.mappers.UserMapper;
import net.thumbtack.onlineshop.database.mybatis.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

public class DaoImplBase {
    protected SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    protected UserMapper getUserMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(UserMapper.class);
    }
    protected CommonMapper getCommonMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(CommonMapper.class);
    }

}
