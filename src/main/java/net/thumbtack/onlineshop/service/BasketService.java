package net.thumbtack.onlineshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.onlineshop.database.dao.BasketDao;
import net.thumbtack.onlineshop.database.mybatis.transfer.ProductDTO;
import net.thumbtack.onlineshop.dto.request.basket.BasketBuyProductRequest;
import net.thumbtack.onlineshop.dto.request.basket.BasketUpdateCountProductRequest;
import net.thumbtack.onlineshop.dto.request.basket.ProductAddInBasketsRequest;
import net.thumbtack.onlineshop.dto.response.basket.BasketBuyProductResponse;
import net.thumbtack.onlineshop.dto.response.basket.BasketResponse;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import net.thumbtack.onlineshop.model.exeptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
public class BasketService {
    private @Autowired ObjectMapper mapper;
    private @Autowired UserService userService;
    private BasketDao basketDao;

    public String addProductInBasket(ProductAddInBasketsRequest dto, String token) throws JsonProcessingException, UserException {
        Client client = userService.getClientByToken(token);
        if(dto.getCount() == 0) {
            dto.setCount(1);
        }
        basketDao.addProductInBasket(client, new ProductDTO(dto.getId(), dto.getName(),  dto.getPrice(), dto.getCount(), null));
        return getProductsInBasket(client);
    }

    public void deleteProductFromBasket(int id, String token) throws UserException {
        Client client = userService.getClientByToken(token);
        basketDao.deleteProductFromBasket(id, client);
    }

    public String updateProductCount(BasketUpdateCountProductRequest dto, String token) throws UserException, JsonProcessingException {
        Client client = userService.getClientByToken(token);
        basketDao.updateProductCount(new ProductDTO(dto.getId(), dto.getName(), dto.getPrice(), dto.getCount(), null), client);
        return getProductsInBasket(client);
    }

    private String getProductsInBasket(Client client) throws JsonProcessingException {
        List<Product> p = basketDao.getProductsInBasket();
        List<BasketResponse> list = new ArrayList<>(p.size());
        for(Product it : p) {
            list.add(new BasketResponse(it.getId(), it.getName(), it.getPrice(), it.getCount()));
        }
        return mapper.writeValueAsString(list);
    }

    public String getProductsInBasket(String token) throws UserException, JsonProcessingException {
        Client client = userService.getClientByToken(token);
        return getProductsInBasket(client);
    }

    public String buyProduct(List<BasketBuyProductRequest> dto, String token) throws UserException {
//        Если  для  какого-то  товара  количество  единиц  в  запросе  не  указано,  то  покупается  то  количество  единиц,  которое  есть сейчас в
//        корзине.
//                Если для какого-то товара указанные в запросе название товара или стоимость за единицу отличаются от текущих значений для этого
//        продукта, запрос для этого продукта не выполняется, товар остается в корзине.
//                Не допускается покупка большего числа единиц, чем имеется в корзине. Если такое имеет место для какого-то товара, то покупается
//        то количество единиц, которое есть сейчас в корзине
//        При  покупке  товара  должно  проверяться  наличие  в  продаже  требуемого количества единиц по каждому товару. Для тех товаров,
//                количество единиц которых, имеющееся в продаже, меньше количества единиц в корзине, запрос не выполняется, товары остаются в
//        корзине.
//                Если в покупку включен товар из числа удаленных, запрос для этого товара не выполняется.
//                Если  суммарная  стоимость  всей  покупки  (с  учетом  вышесказанного)  превышает  количество  денег  на  счете  клиента,  запрос
//        отвергается целиком.
//        В Response возвращаются список купленных товаров и список товаров, оставшихся в корзине.
        Client client = userService.getClientByToken(token);
        new BasketBuyProductResponse();
        return null;
    }
}
