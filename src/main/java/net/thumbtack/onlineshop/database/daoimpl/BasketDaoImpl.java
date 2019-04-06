package net.thumbtack.onlineshop.database.daoimpl;

import net.thumbtack.onlineshop.database.dao.BasketDao;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasketDaoImpl   extends BaseDaoImpl implements BasketDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasketDaoImpl.class);

    @Override
    public void addProductInBasket(Client client, Product product, int count) {
        // Запрос отвергается, если указанные в запросе название товара или стоимость за единицу
        // отличаются от текущих значений для этого  продукта
        LOGGER.debug("BasketDao addProductInBasket");
        try(SqlSession sqlSession = getSession()) {
            try {
                getBasketMapper(sqlSession);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't addProductInBasket in DB ", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public List<Product> getProductsInBasket() {
        return null;
    }

    @Override
    public void deleteProductFromBasket(int id, Client client) {

    }

    @Override
    public void updateProductCount(Product product, Client client) {
        // Запрос отвергается, если указанные в запросе название товара или
        // стоимость за единицу отличаются от текущих значений для этого продукта
        // Допускается изменение количества единиц товара независимо от того, сколько единиц
        // такого товара имеется в продаже , более того, разрешается изменение количества
        // единиц  для удаленного товара. Данный запрос не может добавлять новые товары в корзину.
    }
}
