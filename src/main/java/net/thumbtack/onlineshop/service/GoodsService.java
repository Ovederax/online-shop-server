package net.thumbtack.onlineshop.service;

import com.google.gson.Gson;
import net.thumbtack.onlineshop.dto.response.basket.AddGoodsInBasketsResponse;
import net.thumbtack.onlineshop.dto.response.cathegory.GetCategoryResponse;
import net.thumbtack.onlineshop.dto.response.goods.GoodsResponse;
import net.thumbtack.onlineshop.dto.response.user.GetClientsInfoResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsService {
    private Gson gson;

    public GoodsService() {
        gson = new Gson();
    }
    /**    Имена всех категорий (в том числе подкатегорий) уникальны
    Имя категории не может быть пустым.
    Если поле “parentId” в запросе не указано или
    равно 0, добавляется категория,
    в противном случае добавляется подкатегория к
    категории “parentId”, которая должна существовать.*/
    String addCategory(String jsonReq, String JAVASESSIONID) {
        return null;
    }
    String getCategory(String JAVASESSIONID, int categoryID) {
        return null;
    }
    /**Хотя бы одно из полей запроса не должно быть пустым.
    Если в запросе присутствует поле “name”  - изменяется название
    категории или подкатегории.
    Если в запросе присутствует поле “parentId” - подкатегория
    перемещается в другую категорию.
    Для категорий такой запрос недопустим.*/
    String editCategory(String jsonReq, String JAVASESSIONID, int categoryID) {
        /**Редактирование категории или подкатегории не влияет на находящиеся
        в ней товары - они по-прежнему принадлежат этой категории
        или подкатегории*/
        return null;
    }
    String deleteCategory(String JAVASESSIONID, int categoryID) {
        /**Удаление подкатегории приводит к тому, что все находившиеся
        в нем товары больше к этой подкатегории не принадлежат.
                Сами товары не удаляются.
        Удаление категории приводит к удалению всех ее подкатегорий.
        Все находившиеся в подкатегориях этой категории товары
        больше не принадлежат к этим подкатегориям.
        Если после удаления категории или подкатегории список
        категорий для некоторого товара оказывается пустым, считается,
        что этот товар теперь не принадлежит ни к одной категории или
        подкатегории.*/

        return "{}";
    }

    String getCategoriesList(String JAVASESSIONID) {
        /**Список выдается, отсортированный по именам категорий,
        за каждой из которых следуют ее подкатегории,
        также отсортированные по имени*/
        List<GetCategoryResponse> list = new ArrayList<>();
        return gson.toJson(list);
    }

    String addGoods(String jsonReq, String JAVASESSIONID) {
        /**Названия товаров могут совпадать у разных товаров.
        Если количество единиц товара равно 0 или отсутствует -
        товар создается с количеством единиц, равным 0
        (иными словами, товар вносится в базу, но в наличии его пока нет).
        Цена товара не может быть <=0.
        Список категорий может быть пустым или отсутствовать вообще,
        в этом случае товару устанавливается пустой список категорий.*/

        return null;
    }

    String editGoods(String jsonReq, String JAVASESSIONID,  int goodsID) {
        return null;
    }

    String deleteGoods(String JAVASESSIONID,  int goodsID) {
        /**Удаление товара разрешается даже если количество его
        единиц не равно 0.
        Удаление товара разрешено даже если удаляемый товар
        находится в какой-то корзине покупателя. В случае
        удаления такого товара он из корзины не удаляется,
        но покупка его из корзины становится невозможной.*/
        return "{}";
    }

    String getGoods(String JAVASESSIONID,  int goodsID) {
        GoodsResponse response = null;
        return gson.toJson(response);
    }

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
    String getGoodsList(String JAVASESSIONID,  List<Long> categoriesId, String order) {
        List<GoodsResponse> list = null;
        return gson.toJson(list);
    }

    String putDepositMoney(String jsonReq, String JAVASESSIONID) {
        GetClientsInfoResponse response = null;
        return gson.toJson(response);
    }
    String getDepositMoney(String JAVASESSIONID) {
        GetClientsInfoResponse response = null;
        return gson.toJson(response);
    }

    String buyGoods(String jsonReq, String JAVASESSIONID) {
    /**
     * Если количество единиц в запросе не указано, то оно принимается равным 1.
    Запрос отвергается, если
    ●	 не имеется требуемое количество единиц товара,
    ●	 суммарная стоимость всех единиц товара превышает количество денег на счете клиента,
    ●	 указанные в запросе название товара или стоимость за единицу отличаются от текущих значений для этого продукта*/
        return null;
    }
    String addGoodsInBaskets(String jsonReq, String JAVASESSIONID) {
        /**Если количество единиц в запросе не указано, то оно принимается равным 1.
        Запрос отвергается, если указанные в запросе название товара или стоимость за единицу отличаются от текущих значений для этого продукта
        Допускается добавление в корзину любого количества единиц товара независимо от того, сколько единиц такого товара имеется в продаже.
        Клиент может добавить в корзину любое количество единиц любого товара, независимо от того, сколько денег у него имеется на счете.
        В Response возвращается полный состав корзины.
        */
        return gson.toJson(new ArrayList<AddGoodsInBasketsResponse>());
    }
    String deleteGoodsFromBaskets(String JAVASESSIONID, String goodsId) {
        return "{}";
    }

}
