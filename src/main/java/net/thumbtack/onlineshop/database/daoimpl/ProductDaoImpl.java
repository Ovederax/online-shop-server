package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.ProductDao;
import net.thumbtack.onlineshop.database.mybatis.mappers.BasketMapper;
import net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper;
import net.thumbtack.onlineshop.database.mybatis.mappers.UserMapper;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import net.thumbtack.onlineshop.model.entity.Purchase;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

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
                if(categories != null) {
                    mapper.insertProductCategories(product, categories);
                }
            } catch (Exception ex) {
                LOGGER.info("Can't addProduct {} in DB ", product, ex);
                sqlSession.rollback();
                throw new ServerException(ErrorCode.CANT_ADD_PRODUCT_WITH_NO_UNIQUE_NAME);
            }
            sqlSession.commit();
        }
        return product.getId();
    }

    @Override
    public Product getProductById(int id) {
        LOGGER.debug("ProductDao getProductById");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).findProductById(id);
            } catch (Exception ex) {
                LOGGER.info("Can't getProductById {} in DB ", id, ex);
                throw ex;
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
            } catch (Exception ex) {
                LOGGER.info("Can't updateProduct {} in DB ", product, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void markProductAsDeleted(Product product) {
        LOGGER.debug("ProductDao markProductAsDeleted");
        try(SqlSession sqlSession = getSession()) {
            try {
                getProductMapper(sqlSession).markProductAsDeleted(product);
            } catch (Exception ex) {
                LOGGER.info("Can't markProductAsDeleted in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
    private void buyProductDepositUpdate(UserMapper userMapper, Client client, int newMoneyDeposit) throws ServerException {
        if(userMapper.updateMoneyDeposit(client, newMoneyDeposit) != 1) {
            throw new ServerException(ErrorCode.NO_BUY_IF_CLIENT_DEPOSIT_IS_CHANGE);
        }
    }
    private void buyProductProductUpdate(ProductMapper productMapper, Purchase purchase, int newProductCount) throws ServerException {
        if(productMapper.updateProductCount(purchase.getActual(), newProductCount) != 1) {
            throw new ServerException(ErrorCode.NO_BUY_IF_PRODUCT_IS_CHANGE);
        }
    }
    private void buyProductMakePurchase(ProductMapper productMapper, Purchase purchase, Client client) throws ServerException {
        if(productMapper.makePurchase(purchase, client) != 1) {
            throw new ServerException(ErrorCode.PRODUCT_IS_DELETED);
        }
    }
    private void commonBuyProduct(UserMapper userMapper, ProductMapper productMapper,
                          Purchase purchase, Client client, int newMoneyDeposit, int newProductCount) throws ServerException {
        buyProductDepositUpdate(userMapper, client, newMoneyDeposit);
        buyProductProductUpdate(productMapper, purchase, newProductCount);
        buyProductMakePurchase(productMapper, purchase, client);

        client.getDeposit().setMoney(newMoneyDeposit);
        purchase.getActual().setCounter(newProductCount);
    }
    @Override
    public int buyProduct(Purchase purchase, Client client, int newMoneyDeposit, int newProductCount) throws ServerException {
//        1) Пробуем снять деньги с клиента  *кол-во денег на момент покупки не изменилось
//        2) Пробуем обновить кол-во товара  *свойства товара не изменились
//        3) Добавляем объект покупки
        LOGGER.debug("ProductDao buyProductFromBasket");
        try(SqlSession sqlSession = getSession()) {
            try {
                ProductMapper productMapper = getProductMapper(sqlSession);
                UserMapper userMapper = getUserMapper(sqlSession);
                commonBuyProduct(userMapper, productMapper, purchase, client, newMoneyDeposit, newProductCount);
            } catch (Exception ex) {
                LOGGER.info("Can't buyProductFromBasket {} in DB ", purchase, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return purchase.getId();
    }
    public void deleteItemFromBasketByProductId(BasketMapper mapper, int id) throws ServerException {
        LOGGER.debug("ProductDao deleteItemFromBasketByProductId");
        try {
            mapper.deleteItemFromBasketByProductId(id);
        } catch (Exception ex) {
            LOGGER.info("Can't deleteItemFromBasketByProductId in DB ", ex);
            throw new ServerException(ErrorCode.DB_CANT_DELETE_FROM_BASKET);
        }
    }
    @Override
    public int buyProductFromBasket(Purchase purchase, Client client, int newMoneyDeposit, int newProductCount) throws ServerException {
        LOGGER.debug("ProductDao buyProductFromBasket");
        try(SqlSession sqlSession = getSession()) {
            try {
                ProductMapper productMapper = getProductMapper(sqlSession);
                UserMapper userMapper = getUserMapper(sqlSession);
                BasketMapper basketMapper = getBasketMapper(sqlSession);

                commonBuyProduct(userMapper, productMapper, purchase, client, newMoneyDeposit, newProductCount);
                deleteItemFromBasketByProductId(basketMapper, purchase.getActual().getId());
            } catch (Exception ex) {
                LOGGER.info("Can't buyProductFromBasket {} in DB ", purchase, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return purchase.getId();
    }

    @Override
    public List<Product> getProductListOrderProduct(List<Integer> categoriesId) {
        LOGGER.debug("ProductDao getProductListOrderProduct");
        try(SqlSession sqlSession = getSession()) {
            try {
                if(categoriesId != null) {
                    StringJoiner joiner = new StringJoiner(",");
                    for(Integer it: categoriesId) {
                        joiner.add(it.toString());
                    }
                    return getProductMapper(sqlSession).getProductListOrderProduct(joiner.toString());
                } else {
                    return getProductMapper(sqlSession).getProductListOrderProduct(null);
                }
            } catch (Exception ex) {
                LOGGER.info("Can't getProductListOrderProduct in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public List<Product> getProductListOrderProductNoCategory() {
        LOGGER.debug("ProductDao getProductListOrderProductNoCategory");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getProductListOrderProductNoCategory();
            } catch (Exception ex) {
                LOGGER.info("Can't getProductListOrderProductNoCategory in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public List<Category> getProductListOrderCategory(List<Integer> categoriesId) {
        LOGGER.debug("ProductDao getProductListOrderProduct");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getProductListOrderCategory(categoriesId);
            } catch (Exception ex) {
                LOGGER.info("Can't getProductListOrderProduct in DB ", ex);
                throw ex;
            }
        }
    }

    @Override
    public List<Product> getAllProduct() {
        LOGGER.debug("ProductDao getAllProduct");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).getAllProduct();
            } catch (Exception ex) {
                LOGGER.info("Can't getAllProduct in DB ", ex);
                throw ex;
            }
        }
    }
}
