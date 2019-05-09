package net.thumbtack.onlineshop.database.mybatis.mappers;

import net.thumbtack.onlineshop.model.entity.Category;
import net.thumbtack.onlineshop.model.entity.Client;
import net.thumbtack.onlineshop.model.entity.Product;
import net.thumbtack.onlineshop.model.entity.Purchase;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.sql.SQLException;
import java.util.List;

public interface ProductMapper {
    @Select("SELECT products.id, products.name, products.price, products.counter FROM products " +
            "JOIN products_categories ON productId=#{products.id} WHERE categoryId = #{categoryId}")
    List<Product> findProductsByCategoryId(int categoryId) throws SQLException;

    @Select("SELECT products.id, products.name, products.price, products.counter FROM products " +
            "JOIN products_categories ON productId=#{products.id} WHERE categoryId = #{categoryId}")
    List<Product> findProductsByCategoryIdOrderName(int categoryId) throws SQLException;

    @Insert("INSERT INTO products(name, price, counter, isDeleted) VALUES(#{name}, #{price}, #{counter}, #{isDeleted})")
    @Options(useGeneratedKeys = true)
    void addProduct(Product product) throws SQLException;

    @Insert({"<script>",
            "INSERT INTO products_categories(productId, categoryId) VALUES",
            "<foreach item='item' collection='list' separator=','>",
            "( #{product.id}, #{item} )",
            "</foreach>",
            "</script>"})
    void insertProductCategories(@Param("product") Product product, @Param("list") List<Integer> categories) throws SQLException;

    @Select("SELECT * FROM products WHERE id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findCategoriesByProductId", fetchType = FetchType.LAZY)),
    })
    Product findProductById(int id) throws SQLException;


    @Select("SELECT * FROM products")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findCategoriesByProductId", fetchType = FetchType.LAZY)),
    })
    List<Product> getAllProduct() throws SQLException;

    @Select("SELECT categories.* FROM categories JOIN products_categories ON categories.id=categoryId WHERE productId=#{productId}")
    List<Category> findCategoriesByProductId(int productId) throws SQLException;

    @Update("<script>UPDATE products " +
            "<set>" +
            "<if test='name != null'>name=#{name},</if>" +
            "<if test='price != null'>price=#{price},</if>" +
            "<if test='counter != null'>counter=#{counter}</if>" +
            "</set> " +
            "WHERE id=#{product.id}" +
            "</script>")
    void updateProduct(@Param("product") Product product, @Param("name") String name, @Param("price") Integer price, @Param("counter") Integer counter) throws SQLException;

    @Delete("UPDATE products SET isDeleted=1 WHERE id=#{id}")
    void markProductAsDeleted(Product product) throws SQLException;

    @Update("DELETE FROM products WHERE id=#{id}")
    void deleteProductById(int id) throws SQLException;

    @Delete("DELETE FROM products")
    void deleteAllProduct() throws SQLException;

    @Delete("DELETE FROM products_categories")
    void deleteAllTableProductsCategories() throws SQLException;

    @Delete("DELETE FROM products_categories WHERE productId=#{id}")
    void deleteAllProductCategories(Product product) throws SQLException;


// 1.1 Возвращается список товаров, принадлежащих хотя бы одной из
// указанных категорий.
//
// 1.2 Если список категорий не указан в запросе,
// возвращается полный список товаров, включая товары,
// не относящиеся ни к одной категории.
//
// 1.3 Если передается пустой список
// категорий, выдается список товаров, не относящихся ни к одной
// категории.
//
// Если order = “product”, список выдается, отсортированный по
// именам товаров, и в этом случае в поле “categories” приводится
// список категорий, к которым он относится.
// Каждый товар приводится
// в списке только один раз.


//    Если order = “category”, список выдается,
//    отсортированный по именам категорий, а внутри категории - по именам товаров.
//    В этих случаях каждый товар указывается для каждой своей категории,
//    а в поле categories возвращается лишь одна категория.
//    Товары, не относящиеся ни к одной категории, выдаются в начале
//    списка.

    @Select("<script>SELECT DISTINCT products.* FROM products " +
            "LEFT JOIN products_categories ON products.id=productId" +
            "<if test='list!=null'>" +
            "   WHERE categoryId IN " +
            "       <foreach item='item' collection='list' open='(' separator=',' close=')'>" +
            "           #{item}" +
            "       </foreach>" +
            "   </if>" +
            "ORDER BY products.name" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findCategoriesByProductId", fetchType = FetchType.EAGER)), // сортровка категорий не требуется
    })
    List<Product> getProductListOrderProduct(List<Integer> list) throws SQLException;

    @Select("SELECT * FROM products " +
            "WHERE id NOT IN (SELECT DISTINCT productId FROM products_categories) " +
            "ORDER BY products.name")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "categories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findCategoriesByProductId", fetchType = FetchType.EAGER)),
    })
    List<Product> getProductListOrderProductNoCategory() throws SQLException;


    @Select("<script>" +
            "SELECT * FROM categories " +
            "<if test='categoriesId!=null'>WHERE id IN (#{categoriesId}) </if>" +
            "ORDER BY name" +
            "</script>")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "parent", column = "parentId", javaType = Category.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.CategoryMapper.findCategoryById", fetchType = FetchType.LAZY)),
            @Result(property = "subCategories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.CategoryMapper.findCategoryById", fetchType = FetchType.LAZY)),
            @Result(property = "products", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findProductsByCategoryIdOrderName", fetchType = FetchType.EAGER))
    })
    List<Category> getProductListOrderCategory(List<Integer> categoriesId) throws SQLException;

    @Insert("INSERT INTO purchases(actualId, clientId, name, buyCount, buyPrice)" +
            " VALUES(#{purchase.actual.id}, #{client.id}, #{purchase.name}, #{purchase.buyCount}, #{purchase.buyPrice})")
    @Options(useGeneratedKeys = true, keyProperty = "purchase.id")
    int makePurchase(@Param("purchase") Purchase purchase, @Param("client") Client client) throws SQLException;

    @Update("UPDATE products SET counter=#{newCount} WHERE counter=#{product.counter}")
    int updateProductCount(@Param("product") Product product, @Param("newCount") int newProductCount) throws SQLException;

    @Delete("DELETE FROM purchases")
    void deleteAllPurchases() throws SQLException;


    @Select("SELECT * FROM purchases WHERE clientId=#{clientId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "actual", column = "actualId", javaType = Product.class,
                one = @One(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findProductById", fetchType = FetchType.LAZY))
    })
    List<Purchase> findPurchasesByClientId(int clientId) throws SQLException;

}
