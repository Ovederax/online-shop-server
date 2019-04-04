package net.thumbtack.onlineshop.database.mybatis.mappers;

import net.thumbtack.onlineshop.database.mybatis.transfer.ProductDTO;
import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ProductMapper {
    @Select("SELECT goods.id, goods.name, goods.price, goods.counter FROM goods JOIN goods_categories ON goodsId=#{goods.id} WHERE categoryId = #{categoryId}")
    List<Product> findProductsByCategoryId(int categoryId);

    @Insert("")
    void addProduct(Product product);

    @Insert("")
    void insertProductCategories(Product product, List<Integer> categories);

    @Select("SELECT * FROM goods WHERE id={id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findCategoriesByProductId", fetchType = FetchType.LAZY)),
    })
    Product findProductById(int id);

//    @Select("")
//    @Results({
//            @Result(property = "categories", column = "id", javaType = List.class,
//                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.CategoryMapper.findCategoryById", fetchType = FetchType.LAZY)),
//    })
    List<Category> findCategoriesByProductId(int productId);

    @Update("")
    void updateProduct(ProductDTO dto);

    @Update("")
    void deleteProductById(int id);

    @Delete("DELETE FROM goods")
    void deleteAllProduct();

    @Delete("DELETE FROM goods_categories")
    void deleteAllTableProductsCategories();

}
