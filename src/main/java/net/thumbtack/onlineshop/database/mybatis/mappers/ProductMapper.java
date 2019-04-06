package net.thumbtack.onlineshop.database.mybatis.mappers;

import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface ProductMapper {
    @Select("SELECT products.id, products.name, products.price, products.counter FROM products " +
            "JOIN products_categories ON productId=#{products.id} WHERE categoryId = #{categoryId}")
    List<Product> findProductsByCategoryId(int categoryId);

    @Insert("")
    void addProduct(Product product);

    @Insert("")
    void insertProductCategories(Product product, List<Integer> categories);

    @Select("SELECT * FROM products WHERE id={id}")
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
    void updateProduct(Product dto);

    @Update("")
    void deleteProductById(int id);

    @Delete("DELETE FROM products")
    void deleteAllProduct();

    @Delete("DELETE FROM products_categories")
    void deleteAllTableProductsCategories();

}
