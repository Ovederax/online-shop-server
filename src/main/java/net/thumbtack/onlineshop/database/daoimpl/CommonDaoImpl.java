package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.CommonDao;
import net.thumbtack.onlineshop.database.mybatis.mappers.CategoryMapper;
import net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper;
import net.thumbtack.onlineshop.database.mybatis.mappers.UserMapper;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Product;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommonDaoImpl extends BaseDaoImpl implements CommonDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);

    @Override
    public void clear() {
        LOGGER.debug("DAO clear clients and administrators table DB");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                CategoryMapper categoryMapper = getCategoryMapper(sqlSession);
                ProductMapper productMapper = getProductMapper(sqlSession);

                userMapper.deleteAllClients();
                userMapper.deleteAllAdministrators();
                userMapper.deleteAllLoginRecords();
                userMapper.deleteAllUsers();

                productMapper.deleteAllTableProductsCategories();
                productMapper.deleteAllProduct();

                categoryMapper.deleteAllCategory();

            } catch (RuntimeException ex) {
                LOGGER.info("Can't clear all DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
