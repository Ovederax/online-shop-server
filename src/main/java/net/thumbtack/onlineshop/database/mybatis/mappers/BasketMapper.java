package net.thumbtack.onlineshop.database.mybatis.mappers;

import net.thumbtack.onlineshop.model.entity.BasketItem;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface BasketMapper {
    @Insert("INSERT INTO baskets(userId, productId, count) VALUES(#{client.id}, #{item.product.id}, #{item.count})")
    @Options(useGeneratedKeys = true, keyProperty = "item.id")
    void addProductToBasket(@Param("client") Client client, @Param("item") BasketItem item);


    @Select("SELECT * FROM baskets WHERE userId=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "product", column = "productId", javaType = Product.class,
                    one = @One(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findProductById", fetchType = FetchType.EAGER))
    })
    List<BasketItem> getProductsInBasket(Client client);

    @Delete("DELETE FROM baskets WHERE id=#{id}")
    void deleteItemFromBasketById(int id);

    @Delete("DELETE FROM baskets WHERE productId=#{id}")
    void deleteItemFromBasketByProductId(int id);

    @Select("SELECT * FROM baskets WHERE id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "product", column = "productId", javaType = Product.class,
                    one = @One(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findProductById", fetchType = FetchType.EAGER))
    })
    BasketItem getProductInBasket(int id);

    @Select("<script>" +
            "SELECT * FROM baskets WHERE id IN " +
            "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>" +
            "        #{item}" +
            "  </foreach>" +
            "</script>")
    @Results({
        @Result(property = "product", column = "productId", javaType = Product.class,
                one = @One(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findProductById", fetchType = FetchType.LAZY))
    })
    List<BasketItem> getProductsInBasketByRangeId(@Param("list") List<Integer> list);

    @Update("UPDATE baskets SET count=#{count} WHERE id=#{id}")
    void updateProductCount(BasketItem item);

    @Delete("DELETE FROM baskets")
    void deleteAllBasket();



}
