package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.ProductDao;
import net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper;
import net.thumbtack.onlineshop.database.mybatis.transfer.ProductDTO;
import net.thumbtack.onlineshop.model.entity.Product;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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
                mapper.insertProductCategories(product, categories);
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
                ProductMapper mapper = getProductMapper(sqlSession);
                return mapper.findProductById(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't findProductById {} in DB ", id, ex);
                throw ex;
            }
        }
    }

    @Override
    public void updateProduct(ProductDTO dto) {
        LOGGER.debug("ProductDao updateProduct");
        try(SqlSession sqlSession = getSession()) {
            try {
                ProductMapper mapper = getProductMapper(sqlSession);
                mapper.updateProduct(dto);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't updateProduct {} in DB ", dto, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteProductById(int id) {
        /**Удаление товара разрешается даже если количество его
         единиц не равно 0.
         Удаление товара разрешено даже если удаляемый товар
         находится в какой-то корзине покупателя. В случае
         удаления такого товара он из корзины не удаляется,
         но покупка его из корзины становится невозможной.*/
        LOGGER.debug("ProductDao deleteProductById");
        try(SqlSession sqlSession = getSession()) {
            try {
                ProductMapper mapper = getProductMapper(sqlSession);
                mapper.deleteProductById(id);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't deleteProductById in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Product> getProductsList(List<Integer> categoriesId, String order) {
        /** Возвращается список товаров, принадлежащих хотя бы одной из
         указанных категорий. Если список категорий не указан в запросе,
         возвращается полный список товаров, включая товары, не
         относящиеся ни к одной категории. Если передается пустой список
         категорий, выдается список товаров, не относящихся ни к одной
         категории.
         Если order = “product”, список выдается, отсортированный по
         именам товаров, и в этом случае в поле “categories” приводится
         список категорий, к которым он относится. Каждый товар приводится
         в списке только один раз.
         Если order = “category”, список выдается, отсортированный по
         именам категорий, а внутри категории - по именам товаров.
         В этих случаях каждый товар указывается для каждой своей категории,
         а в поле categories возвращается лишь одна категория.
         Товары, не относящиеся ни к одной категории, выдаются в начале
         списка.
         Если order не присутствует в запросе,то считается,
         что order = “product”. */
        LOGGER.debug("ProductDao getProductsList");
        try(SqlSession sqlSession = getSession()) {
            try {

            } catch (RuntimeException ex) {
                LOGGER.info("Can't getProductsList in DB ", ex);
                throw ex;
            }
        }
        return null;
    }

    @Override
    public List<Product> buyProduct(int userId, ProductDTO productDTO) {
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
                LOGGER.info("Can't buyProduct {} in DB ", productDTO, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return null;
    }
}
