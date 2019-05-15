package net.thumbtack.onlineshop.database.daoimpl;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import net.thumbtack.onlineshop.database.dao.ProductDao;
import net.thumbtack.onlineshop.database.mybatis.mappers.BasketMapper;
import net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper;
import net.thumbtack.onlineshop.database.mybatis.mappers.UserMapper;
import net.thumbtack.onlineshop.model.entity.*;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import net.thumbtack.onlineshop.model.transfer.PurchaseBuyInfo;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDaoImpl extends BaseDaoImpl implements ProductDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);

    @Override
    public int addProduct(Product product, List<Integer> categories) throws ServerException {
        LOGGER.debug("ProductDao addProduct");
        try(SqlSession sqlSession = getSession()) {
            try {
                ProductMapper mapper = getProductMapper(sqlSession);
                mapper.addProduct(product);
                if(categories != null && categories.size() > 0) {
                    mapper.insertProductCategories(product, categories);
                }
            } catch (MySQLIntegrityConstraintViolationException ex) {
                LOGGER.info("Can't addProduct {} in DB ", product, ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_ADD_PRODUCT_WITH_NO_UNIQUE_NAME);
            } catch (SQLException e) {
                LOGGER.info("Can't addProduct {} in DB ", product);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_ADD_PRODUCT);
            }
            sqlSession.commit();
        }
        return product.getId();
    }

    @Override
    public Product getProductById(int id) throws ServerException {
        LOGGER.debug("ProductDao getProductById");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).findProductById(id);
            } catch ( SQLException ex ) {
                LOGGER.info("Can't getProductById {} in DB ", id, ex);
                throw new ServerException(ErrorCode.CANT_GET_PRODUCT);
            }
        }
    }

    @Override
    public void updateProduct(Product product, String name, Integer price, Integer counter, List<Category> categories) throws ServerException {
        LOGGER.debug("ProductDao updateProduct");
        try(SqlSession sqlSession = getSession()) {
            try {
                ProductMapper mapper = getProductMapper(sqlSession);
                mapper.updateProduct(product, name, price, counter);

                if(categories != null) {
                    mapper.deleteAllProductCategories(product);
                    if(categories.size() != 0) {
                        //Неописанное поведение в ТЗ
                        throw new ServerException(ErrorCode.UPDATE_PRODUCT_SET_CATEGORIES_NO_SUPPORT);
                        //mapper.insertProductCategories(product, categories);
                    }
                }
            } catch (SQLException ex) {
                LOGGER.info("Can't updateProduct {} in DB ", product, ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_UPDATE_PRODUCT);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void markProductAsDeleted(Product product) throws ServerException {
        LOGGER.debug("ProductDao markProductAsDeleted");
        try(SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).markProductAsDeleted(product);
            } catch (SQLException ex) {
                LOGGER.info("Can't markProductAsDeleted in DB ", ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_DELETE_PRODUCT);
            }
            sqlSession.commit();
        }
    }

    @Override
    public int buyProduct(Purchase purchase, Client client, int newMoneyDeposit, int newProductCount) throws ServerException {
//        1) Пробуем снять деньги с клиента  *кол-во денег на момент покупки не изменилось
//        2) Пробуем обновить кол-во товара  *свойства товара не изменились
//        3) Добавляем объект покупки
//        4) Удаляем объект корзины
        LOGGER.debug("ProductDao buyProduct");
        try(SqlSession sqlSession = getSession()) {
            try {
                ProductMapper productMapper = getProductMapper(sqlSession);
                UserMapper userMapper = getUserMapper(sqlSession);
                buyProductDepositUpdate(userMapper, client, newMoneyDeposit);
                commonBuyProduct(productMapper, purchase, client, newProductCount);
            } catch (ServerException ex) {
                LOGGER.info("Can't buyProduct {} in DB ", purchase, ex);
                sqlSession.rollback();
                throw ex;
            } catch (SQLException ex) {
                LOGGER.info("Can't buyProduct {} in DB ", purchase, ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_BUY_PRODUCT);
            }
            sqlSession.commit();
        }
        return purchase.getId();
    }

    @Override
    public void buyProductsFromBasket(List<PurchaseBuyInfo> purchases, Client client, int newDeposit) throws ServerException {
        LOGGER.debug("ProductDao buyProductsFromBasket");
        try(SqlSession sqlSession = getSession()) {
            try {
                ProductMapper productMapper = getProductMapper(sqlSession);
                UserMapper userMapper = getUserMapper(sqlSession);
                BasketMapper basketMapper = getBasketMapper(sqlSession);
                buyProductDepositUpdate(userMapper, client, newDeposit);
                for(PurchaseBuyInfo it : purchases) {
                    commonBuyProduct(productMapper, it.getPurchase(), client, it.getNewProductCount());
                    deleteItemFromBasketById(basketMapper, it.getBasketItem().getId());
                }
            } catch (ServerException ex) {
                LOGGER.info("Can't buyProductsFromBasket {} in DB ", ex);
                sqlSession.rollback();
                throw ex;
            } catch (SQLException ex) {
                LOGGER.info("Can't buyProductsFromBasket {} in DB ",  ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_BUY_PRODUCT_FROM_BASKET);
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Product> getProductListOrderProduct(List<Integer> categoriesId) throws ServerException {
        LOGGER.debug("ProductDao getProductListOrderProduct");
        try(SqlSession sqlSession = getSession()) {
            try {
                if(categoriesId != null && categoriesId.size() != 0) {
                    return getProductMapper(sqlSession).getProductListOrderProduct(categoriesId);
                } else {
                    return getProductMapper(sqlSession).getProductListOrderProduct(null);
                }
            } catch (SQLException ex) {
                LOGGER.info("Can't getProductListOrderProduct in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_PRODUCT_LIST);
            }
        }
    }

    @Override
    public List<Product> getProductListOrderProductNoCategory() throws ServerException {
        LOGGER.debug("ProductDao getProductListOrderProductNoCategory");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getProductListOrderProductNoCategory();
            } catch (SQLException ex) {
                LOGGER.info("Can't getProductListOrderProductNoCategory in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_PRODUCT_LIST);
            }
        }
    }

    @Override
    public List<Category> getProductListOrderCategory(List<Integer> categoriesId) throws ServerException {
        LOGGER.debug("ProductDao getProductListOrderProduct");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getProductListOrderCategory(categoriesId);
            } catch (SQLException ex) {
                LOGGER.info("Can't getProductListOrderProduct in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_PRODUCT_LIST);
            }
        }
    }

    @Override
    public List<Product> getAllProduct() throws ServerException {
        LOGGER.debug("ProductDao getAllProduct");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getAllProduct();
            } catch (SQLException ex) {
                LOGGER.info("Can't getAllProduct in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_PRODUCT_LIST);
            }
        }
    }

    @Override
    public List<Purchase> getPurchasesByProductsId(List<Integer> products) throws ServerException {
        LOGGER.debug("ProductDao getPurchasesByProductsId");
        try(SqlSession sqlSession = getSession()) {
            try {
                if(products.size() == 0) {
                    return new ArrayList<>();
                }
                return getProductMapper(sqlSession).getPurchasesByProductsId(products);
            } catch (SQLException ex) {
                LOGGER.info("Can't getPurchasesByProductsId in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_PURCHASES_LIST);
            }
        }
    }

    @Override
    public List<Purchase> getPurchasesByProducts(List<Product> products) throws ServerException {
        LOGGER.debug("ProductDao getPurchasesByProducts");
        try(SqlSession sqlSession = getSession()) {
            try {
                if(products.size() == 0) {
                    return new ArrayList<>();
                }
                return getProductMapper(sqlSession).getPurchasesByProducts(products);
            } catch (SQLException ex) {
                LOGGER.info("Can't getPurchasesByProducts in DB ", ex);
                throw new ServerException(ErrorCode.CANT_GET_PURCHASES_LIST);
            }
        }
    }

    private void buyProductDepositUpdate(UserMapper userMapper, Client client, int newMoneyDeposit) throws ServerException, SQLException {
        if(userMapper.updateMoneyDeposit(client, newMoneyDeposit) != 1) {
            throw new ServerException(ErrorCode.NO_BUY_IF_CLIENT_DEPOSIT_IS_CHANGE);
        }
        client.getDeposit().setMoney(newMoneyDeposit);
    }

    private void buyProductProductUpdate(ProductMapper productMapper, Purchase purchase, int newProductCount) throws ServerException, SQLException {
        if(productMapper.updateProductCount(purchase.getActual(), newProductCount) != 1) {
            throw new ServerException(ErrorCode.NO_BUY_IF_PRODUCT_IS_CHANGE);
        }
    }

    private void buyProductMakePurchase(ProductMapper productMapper, Purchase purchase, Client client) throws ServerException, SQLException {
        if(productMapper.makePurchase(purchase, client) != 1) {
            throw new ServerException(ErrorCode.PRODUCT_IS_DELETED);
        }
    }

    private void commonBuyProduct(ProductMapper productMapper,
                                  Purchase purchase, Client client, int newProductCount) throws ServerException, SQLException {
        buyProductProductUpdate(productMapper, purchase, newProductCount);
        buyProductMakePurchase(productMapper, purchase, client);
        purchase.getActual().setCounter(newProductCount);
    }

    private void deleteItemFromBasketById(BasketMapper mapper, int id) throws ServerException {
        LOGGER.debug("ProductDao deleteItemFromBasketById");
        try {
            mapper.deleteItemFromBasketById(id);
        } catch (SQLException ex) {
            LOGGER.info("Can't deleteItemFromBasketById in DB ", ex);
            throw new ServerException(ErrorCode.CANT_DELETE_FROM_BASKET);
        }
    }
}
