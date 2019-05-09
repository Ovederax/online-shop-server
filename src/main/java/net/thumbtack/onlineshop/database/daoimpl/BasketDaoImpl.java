package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.BasketDao;
import net.thumbtack.onlineshop.model.entity.BasketItem;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BasketDaoImpl   extends BaseDaoImpl implements BasketDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasketDaoImpl.class);

    @Override
    public int addProductToBasket(Client client, BasketItem item) throws ServerException {
        LOGGER.debug("BasketDao addProductToBasket");
        try(SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).addProductToBasket(client, item);
            } catch (SQLException ex) {
                LOGGER.info("Can't addProductToBasket in DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_ADD_PRODUCT_TO_BASKET);
            }
            sqlSession.commit();
        }
        return item.getId();
    }

    @Override
    public List<BasketItem> getProductsInBasket(Client client) throws ServerException {
        LOGGER.debug("BasketDao getProductsInBasket");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getBasketMapper(sqlSession).getProductsInBasket(client);
            } catch (SQLException ex) {
                LOGGER.info("Can't getProductsInBasket in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_PRODUCT_FROM_BASKET);
            }
        }
    }

    @Override
    public void deleteItemFromBasketById(int id) throws ServerException {
        LOGGER.debug("BasketDao deleteItemFromBasketById");
        try(SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).deleteItemFromBasketById(id);
            } catch (SQLException ex) {
                LOGGER.info("Can't deleteItemFromBasketById in DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_DELETE_FROM_BASKET);
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<BasketItem> getProductInBasketInRange(Client client, List<Integer> productsId) throws ServerException {
        LOGGER.debug("BasketDao getProductInBasketInRange");
        try(SqlSession sqlSession = getSession()) {
            try {
                if(productsId.size() == 0) {
                    return new ArrayList<>();
                }
                return getBasketMapper(sqlSession).getProductsInBasketByRangeId(productsId);
            } catch (SQLException ex) {
                LOGGER.info("Can't getProductInBasketInRange in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_PRODUCT_FROM_BASKET);
            }
        }
    }

    @Override
    public void updateProductCount(BasketItem item) throws ServerException {
        LOGGER.debug("BasketDao updateProductCount");
        try(SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).updateProductCount(item);
            } catch (SQLException ex) {
                LOGGER.info("Can't updateProductCount in DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_UPDATE_BASKET_ITEM);
            }
            sqlSession.commit();
        }
    }

    @Override
    public BasketItem getProductInBasket(int id) throws ServerException {
        LOGGER.debug("BasketDao getProductInBasket");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getBasketMapper(sqlSession).getProductInBasket(id);
            } catch (SQLException ex) {
                LOGGER.info("Can't getProductInBasket in DB ", ex);
                throw new ServerException(ErrorCode.CANT_UPDATE_BASKET_ITEM);
            }
        }
    }
}
