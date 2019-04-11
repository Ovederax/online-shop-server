package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.ProductDao;
import net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import net.thumbtack.onlineshop.model.exeptions.ServerException;
import net.thumbtack.onlineshop.model.exeptions.enums.ErrorCode;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

@Component
public class ProductDaoImpl extends BaseDaoImpl implements ProductDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);

    @Override
    public int addProduct(Product product, List<Integer> categories) {
        LOGGER.debug("ProductDao addProduct");
        try(SqlSession sqlSession = getSession()) {
            try {
                ProductMapper mapper = getProductMapper(sqlSession);
                mapper.addProduct(product);
                if(categories != null) {
                    mapper.insertProductCategories(product, categories);
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't addProduct {} in DB ", product, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return product.getId();
    }

    @Override
    public Product findProductById(int id) {
        LOGGER.debug("ProductDao findProductById");
        try(SqlSession sqlSession = getSession()) {
            try {
                return getProductMapper(sqlSession).findProductById(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findProductById {} in DB ", id, ex);
                throw ex;
            }
        }
    }

    @Override
    public void updateProduct(Product product, String name, Integer price, Integer counter, List<Integer> categories) throws ServerException {
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
            } catch (RuntimeException ex) {
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
            } catch (RuntimeException ex) {
                LOGGER.info("Can't markProductAsDeleted in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Product> buyProduct(Client userId, Product product, Integer count) {
        //    Если количество единиц в запросе не указано, то оно принимается равным 1.
//    Запрос отвергается, если
//    ●  не имеется требуемое количество единиц товара,
//    ●  суммарная стоимость всех единиц товара превышает количество денег на счете клиента,
//    ●  указанные в запросе название товара или стоимость за единицу отличаются от текущих значений для этого продукта
        LOGGER.debug("ProductDao buyProduct");
        try(SqlSession sqlSession = getSession()) {
            try {
                ProductMapper mapper = getProductMapper(sqlSession);

            } catch (RuntimeException ex) {
                LOGGER.info("Can't buyProduct {} in DB ", product, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return null;
    }

    @Override
    public List<Product> getProductListOrderProduct(List<Integer> categoriesId) {
        LOGGER.debug("ProductDao getProductListOrderProduct");
        try(SqlSession sqlSession = getSession()) {
            try {
                //if(categoriesId.size() != 0)
                StringJoiner joiner = new StringJoiner(",");
                for(Integer it: categoriesId) {
                    joiner.add(it.toString());
                }
                return getProductMapper(sqlSession).getProductListOrderProduct(joiner.toString());
            } catch (RuntimeException ex) {
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
            } catch (RuntimeException ex) {
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
            } catch (RuntimeException ex) {
                LOGGER.info("Can't getProductListOrderProduct in DB ", ex);
                throw ex;
            }
        }
    }
}
