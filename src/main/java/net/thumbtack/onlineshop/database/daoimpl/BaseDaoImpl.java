package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.mybatis.mappers.*;
import net.thumbtack.onlineshop.database.mybatis.utils.MyBatisUtils;
import net.thumbtack.onlineshop.model.entity.Category;
import org.apache.ibatis.session.SqlSession;

public class BaseDaoImpl {
    protected SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    protected UserMapper getUserMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(UserMapper.class);
    }
    protected CategoryMapper getCategoryMapper(SqlSession sqlSession) { return sqlSession.getMapper(CategoryMapper.class); }
    protected ProductMapper getProductMapper(SqlSession sqlSession) { return sqlSession.getMapper(ProductMapper.class); }
    protected BasketMapper getBasketMapper(SqlSession sqlSession) { return sqlSession.getMapper(BasketMapper.class); }
    protected DepositMapper getDepositMapper(SqlSession sqlSession) { return sqlSession.getMapper(DepositMapper.class); }

}
