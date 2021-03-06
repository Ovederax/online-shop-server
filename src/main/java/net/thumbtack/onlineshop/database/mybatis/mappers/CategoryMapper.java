package net.thumbtack.onlineshop.database.mybatis.mappers;

import net.thumbtack.onlineshop.model.entity.Category;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.sql.SQLException;
import java.util.List;

public interface CategoryMapper {
    @Insert("<script>" +
            "INSERT INTO categories(name, parentId) VALUES(#{name}, " +
            "<if test='parent!=null'>#{parent.id}</if>" +
            "<if test='parent==null'>null</if>"+            // как это сделать правильно?
            ")"+
            "</script>")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void insertCategory(Category category) throws SQLException;

    @Select("SELECT * FROM categories WHERE id=#{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "parent", column = "parentId", javaType = Category.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.CategoryMapper.findCategoryById", fetchType = FetchType.LAZY)),
            @Result(property = "subCategories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.CategoryMapper.findSubCategoryByParentId", fetchType = FetchType.LAZY)),
            @Result(property = "products", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findProductsByCategoryId", fetchType = FetchType.LAZY))
    })
    Category findCategoryById(int id) throws SQLException;

    @Update("<script>update categories "
            + "<set>"
            + "<if test='name!=null'>name=#{name},</if>"
            + "<if test='parentId!=null'>parentId=#{parentId},</if>"
            + "</set> WHERE id=#{id}"
            + "</script>")
    void updateCategoryById(@Param(value = "id") int id, @Param(value = "name") String name, @Param(value = "parentId") Integer parentId) throws SQLException;

    @Delete("DELETE FROM categories WHERE id=#{id}")
    void deleteCategoryById(int id) throws SQLException;

    @Select("SELECT * FROM categories WHERE parentId=#{parentId} ORDER BY name")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "parent", column = "parentId", javaType = Category.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.CategoryMapper.findCategoryById", fetchType = FetchType.LAZY)),
            @Result(property = "products", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findProductsByCategoryId", fetchType = FetchType.LAZY))
    })
    List<Category> findSubCategoryByParentId(int parentId) throws SQLException;

    @Select("SELECT * FROM categories WHERE parentId IS NULL ORDER BY name")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "subCategories", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.CategoryMapper.findSubCategoryByParentId", fetchType = FetchType.LAZY)),
            @Result(property = "products", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.onlineshop.database.mybatis.mappers.ProductMapper.findProductsByCategoryId", fetchType = FetchType.LAZY))
    })
    List<Category> getParentsCategories() throws SQLException;

    @Delete("DELETE FROM categories")
    void deleteAllCategory() throws SQLException;

    @Select("SELECT * FROM categories WHERE id IN (#{categories})")
    List<Category> findCategoriesById(String categories) throws SQLException;
}
