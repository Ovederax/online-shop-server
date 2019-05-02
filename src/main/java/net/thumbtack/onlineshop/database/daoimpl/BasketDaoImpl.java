package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.BasketDao;
import net.thumbtack.onlineshop.model.entity.BasketItem;
import net.thumbtack.onlineshop.model.entity.Client;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BasketDaoImpl   extends BaseDaoImpl implements BasketDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasketDaoImpl.class);

    @Override
    public int addProductToBasket(Client client, BasketItem item) {
        LOGGER.debug("BasketDao addProductToBasket");
        try(SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).addProductToBasket(client, item);
            } catch (Exception ex) {
                LOGGER.info("Can't addProductToBasket in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return item.getId();
    }

    @Override
    public List<BasketItem> getProductsInBasket(Client client) {
        LOGGER.debug("BasketDao getProductsInBasket");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getBasketMapper(sqlSession).getProductsInBasket(client);
            } catch (Exception ex) {
                LOGGER.info("Can't getProductsInBasket in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public void deleteItemFromBasketById(int id) {
        LOGGER.debug("BasketDao deleteItemFromBasketById");
        try(SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).deleteItemFromBasketById(id);
            } catch (Exception ex) {
                LOGGER.info("Can't deleteItemFromBasketById in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<BasketItem> getProductInBasketInRange(Client client, List<Integer> productsId) {
        LOGGER.debug("BasketDao getProductInBasketInRange");
        try(SqlSession sqlSession = getSession()) {
            try {
                if(productsId.size() == 0) {
                    return new ArrayList<>();
                }
                return getBasketMapper(sqlSession).getProductsInBasketByRangeId(productsId);
            } catch (Exception ex) {
                LOGGER.info("Can't getProductInBasketInRange in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public void updateProductCount(BasketItem item) {
        LOGGER.debug("BasketDao updateProductCount");
        try(SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession).updateProductCount(item);
            } catch (Exception ex) {
                LOGGER.info("Can't updateProductCount in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public BasketItem getProductInBasket(int id) {
        LOGGER.debug("BasketDao getProductInBasket");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getBasketMapper(sqlSession).getProductInBasket(id);
            } catch (Exception ex) {
                LOGGER.info("Can't getProductInBasket in DB ", ex);
                throw ex;
            }
        }
    }
}
