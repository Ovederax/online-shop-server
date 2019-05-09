package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.CommonDao;
import net.thumbtack.onlineshop.database.mybatis.mappers.*;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class CommonDaoImpl extends BaseDaoImpl implements CommonDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonDaoImpl.class);

    @Override
    public void clear() throws ServerException {
        LOGGER.debug("DAO clear clients and administrators table DB");
        try(SqlSession sqlSession = getSession()) {
            try {
                UserMapper userMapper = getUserMapper(sqlSession);
                CategoryMapper categoryMapper = getCategoryMapper(sqlSession);
                ProductMapper productMapper = getProductMapper(sqlSession);
                DepositMapper depositMapper = getDepositMapper(sqlSession);
                BasketMapper basketMapper = getBasketMapper(sqlSession);

                productMapper.deleteAllPurchases();
                basketMapper.deleteAllBasket();

                depositMapper.clearAllDeposits();
                userMapper.deleteAllClients();
                userMapper.deleteAllAdministrators();
                userMapper.deleteAllLoginRecords();
                userMapper.deleteAllUsers();

                productMapper.deleteAllTableProductsCategories();
                productMapper.deleteAllProduct();

                categoryMapper.deleteAllCategory();

            } catch (SQLException ex) {
                LOGGER.info("Can't clear all DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_CLEAR_DATABASE);
            }
            sqlSession.commit();
        }
    }
}
